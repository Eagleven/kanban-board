document.addEventListener('DOMContentLoaded', function () {
  function fetchUserBoards() {
    const accessToken = localStorage.getItem('AccessToken');
    console.log(accessToken);

    $.ajax({
      crossOrigin: true,
      type: 'GET',
      url: '/boards?page=1',
      headers: {
        'AccessToken': `${accessToken}`,
        'Content-Type': 'application/json'
      },
      success: function (response) {
        const boards = response.data.content;
        displayUserBoards(boards);

        if (boards.length > 0) {
          const firstBoardId = boards[0].id;
          fetchBoardDetails(firstBoardId, boards[0]);
        }
      },
      error: function (xhr, status, error) {
        console.error('보드 리스트를 가져오는 데 실패했습니다:', error);
      }
    });
  }

  function fetchBoardDetails(boardId, board) {
    const accessToken = localStorage.getItem('AccessToken');

    $.ajax({
      crossOrigin: true,
      type: 'GET',
      url: `/${boardId}/column`,
      headers: {
        'AccessToken': `${accessToken}`,
        'Content-Type': 'application/json'
      },
      success: function (response) {
        console.log('보드 상세 정보:', response.data);
        displayBoardDetails(response.data, board);
      },
      error: function (xhr, status, error) {
        console.error('보드 상세 정보를 가져오는 데 실패했습니다:', error);
      }
    });
  }

  function displayUserBoards(boards) {
    const boardsList = $('#boards-list');
    boardsList.empty();

    boards.forEach(board => {
      const boardItem = $(`
        <div class="board-item" data-board-id="${board.id}">
          <h3 class="board-name">${board.name}</h3>
          <i class="trash icon trash-icon" data-board-id="${board.id}"></i>
        </div>
      `);
      boardItem.on('click', function (event) {
        if (!$(event.target).hasClass('trash-icon')) {
          const boardId = $(this).data('board-id');
          fetchBoardDetails(boardId, board);
        }
      });
      boardItem.find('.trash-icon').on('click', function () {
        const boardId = $(this).data('board-id');
        if (confirm('삭제하는 경우 연결된 데이터가 전부 삭제됩니다. 정말 삭제하시겠습니까?')) {
          deleteBoard(boardId);
        }
      });
      boardsList.append(boardItem);
    });
  }

  function displayBoardDetails(columns, board) {
    if (!Array.isArray(columns)) {
      console.error('Invalid columns data:', columns);
      return;
    }

    const selectedBoard = $('#selected-board');
    selectedBoard.empty();

    const boardContent = `
      <div class="board-details">
        <h2 class="board-header">${board.name}
        <i class="edit icon edit-board-icon" id="edit-board-icon"></i></h2>
        <p>${board.explanation}</p>
        <div class="board-columns" id="board-columns">
          ${columns.map(column => `
            <div class="board-column" data-column-id="${column.id}">
              <h3 class="list-title">
                <span>${column.name}</span>
                <div class="dropdown">
                  <i class="ellipsis horizontal icon dropdown-icon"></i>
                  <div class="dropdown-content">
                    <button class="edit-column">Edit</button>
                    <button class="delete-column">Delete</button>
                  </div>
                </div>
              </h3>
              <div class="board-cards">
                ${Array.isArray(column.cards) ? column.cards.map(card => `
                  <div class="board-card card" data-card-id="${card.id}">
                    <p>${card.title}</p><button class="delete-card">&times;</button>
                  </div>
                `).join('') : ''}
                <button class="add-card">Add Card</button>
              </div>
            </div>
          `).join('')}
          <div class="add-column" id="add-column">
            <i class="plus icon"></i>
            Add another list
          </div>
        </div>
      </div>
    `;
    selectedBoard.append(boardContent);

    // 드롭다운 토글 이벤트 추가
    $('.dropdown-icon').on('click', function() {
      $('.dropdown-content').hide(); // 다른 드롭다운 메뉴 숨김
      $(this).siblings('.dropdown-content').toggle();
    });

    // 다른 곳 클릭 시 드롭다운 메뉴 숨김
    $(document).on('click', function(event) {
      if (!$(event.target).closest('.dropdown').length) {
        $('.dropdown-content').hide();
      }
    });

    $('#edit-board-icon').on('click', function () {
      editBoardDetails(board);
    });

    // 칼럼 수정 이벤트 추가
    $('.edit-column').on('click', function () {
      const columnId = $(this).closest('.board-column').data('column-id');
      editColumn(columns, board, columnId);
    });

    // 칼럼 삭제 이벤트 추가
    $('.delete-column').on('click', function () {
      const columnId = $(this).closest('.board-column').data('column-id');
      if (confirm('삭제하는 경우 연결된 데이터가 전부 삭제됩니다. 정말 삭제하시겠습니까?')) {
        deleteColumn(columnId, columns, board);
      }
    });

    const columnDrake = dragula([document.getElementById('board-columns')], {
      direction: 'horizontal',
      moves: function (el, container, handle) {
        return handle.classList.contains('board-column') || handle.tagName === 'H3';
      },
      invalid: function (el, handle) {
        return el.classList.contains('add-column') || handle.classList.contains('add-column'); // "Add another list" 칸은 드래그 불가
      }
    });
    columnDrake.on('drop', (el, target, source, sibling) => {
      if (el.nextElementSibling && el.nextElementSibling.classList.contains('add-column')) {
        target.insertBefore(el, el.nextElementSibling);
      } else if (el.parentNode === target && sibling && sibling.classList.contains('add-column')) {
        target.insertBefore(el, sibling);
      } else if (!el.nextElementSibling) {
        target.insertBefore(el, document.getElementById('add-column'));
      }

      const columnId = el.getAttribute('data-column-id');
      const newPosition = Array.prototype.indexOf.call(target.children, el);

      updateColumnPosition(columnId, newPosition);
    });

    $('.board-card').on('click', function () {
      const boardId = $(this).data('board-id');
      const columnId = $(this).data('column-id');
      window.location.href = `/card.html?boardId=${boardId}&columnId=${columnId}`;
    });

    $('#add-column').on('click', function () {
      addNewColumn(columns, board); // 여기서 boardId는 부모 스코프에서 접근할 수 있도록 해야 함
    });

    $('.delete-card').on('click', function () {
      $(this).parent().remove();
    });

    // 리스트 삭제 이벤트 추가
    $('.delete-list').on('click', function () {
      $(this).closest('.board-column').remove();
    });

    // 새로운 카드 추가 이벤트 추가
    $('.add-card').on('click', function () {
      const columnId = $(this).closest('.board-column').data('column-id');
      addNewCard(columnId, $(this).closest('.board-cards'));
    });
  }

  function updateCardPosition(cardId, columnId, position) {
    $.ajax({
      type: 'POST',
      url: '/update-card-position',
      data: JSON.stringify({
        cardId: cardId,
        columnId: columnId,
        position: position
      }),
      contentType: 'application/json',
      success: function (response) {
        console.log('카드 위치 업데이트 성공:', response);
      },
      error: function (xhr, status, error) {
        console.error('카드 위치 업데이트 실패:', error);
      }
    });
  }

  function updateColumnPosition(columnId, position) {
    $.ajax({
      type: 'POST',
      url: '/update-column-position',
      data: JSON.stringify({
        columnId: columnId,
        position: position
      }),
      contentType: 'application/json',
      success: function (response) {
        console.log('칼럼 위치 업데이트 성공:', response);
      },
      error: function (xhr, status, error) {
        console.error('칼럼 위치 업데이트 실패:', error);
      }
    });
  }

  function addNewColumn(columns, board) {
    const newColumnName = prompt("Enter the name of the new column:");
    const accessToken = localStorage.getItem('AccessToken');
    if (newColumnName) {
      $.ajax({
        type: 'POST',
        url: `/${board.id}/column`,
        headers: {
          'AccessToken': `${accessToken}`
        },
        data: JSON.stringify({
          name: newColumnName
        }),
        contentType: 'application/json',
        success: function (response) {
          console.log('새 칼럼 추가 성공:', response);
          columns.push(response.data);
          displayBoardDetails(columns, board);
        },
        error: function (xhr, status, error) {
          console.error('새 칼럼 추가 실패:', error);
        }
      });
    }
  }

  function deleteColumn(columnId, columns, board) {
    const accessToken = localStorage.getItem('AccessToken');
    $.ajax({
      type: 'DELETE',
      url: `/${board.id}/column/${columnId}`,
      headers: {
        'AccessToken': `${accessToken}`,
        'Content-Type': 'application/json'
      },
      success: function(response) {
        console.log('칼럼 삭제 성공:', response);
        // 삭제된 칼럼을 columns 배열에서 제거
        const updatedColumns = columns.filter(column => column.id !== columnId);
        // 업데이트된 columns 배열로 보드 세부 정보를 다시 렌더링
        displayBoardDetails(updatedColumns, board);
      },
      error: function(xhr, status, error) {
        console.error('칼럼 삭제 실패:', error);
      }
    });
  }

  function addNewCard(columnId, columnElement) {
    const newCardTitle = prompt("Enter the title of the new card:");
    const accessToken = localStorage.getItem('AccessToken');
    if (newCardTitle) {
      $.ajax({
        type: 'POST',
        url: `/cards/${columnId}`,
        headers: {
          'AccessToken': `${accessToken}`
        },
        data: JSON.stringify({
          title: newCardTitle
        }),
        contentType: 'application/json',
        success: function (response) {
          console.log('새 카드 추가 성공:', response);
          const card = $(`
            <div class="board-card card" data-card-id="${response.data.id}">
              <p>${newCardTitle}</p><button class="delete-card">&times;</button>
            </div>
          `);
          card.insertBefore(columnElement.find('.add-card'));
          card.find('.delete-card').on('click', function () {
            $(this).parent().remove();
          });

          // Add the new card to the appropriate column in the global state
          const columnIndex = columns.findIndex(column => column.id === columnId);
          if (columnIndex !== -1) {
            columns[columnIndex].cards.push(response.data);
          }
        },
        error: function (xhr, status, error) {
          console.error('새 카드 추가 실패:', error);
        }
      });
    }
  }

  function addNewBoard() {
    console.log("addNewBoard");
    const newBoardName = prompt("Enter the name of the new board:");
    const newBoardExplanation = prompt("Enter the explanation of the new board:");

    if (newBoardName && newBoardExplanation) {
      const accessToken = localStorage.getItem('AccessToken');

      $.ajax({
        type: 'POST',
        url: '/boards',
        headers: {
          'AccessToken': `${accessToken}`
        },
        contentType: 'application/json',
        data: JSON.stringify({
          name: newBoardName,
          explanation: newBoardExplanation
        }),
        success: function (response) {
          console.log('새 보드 추가 성공:', response);
          fetchUserBoards();
        },
        error: function (xhr, status, error) {
          console.error('새 보드 추가 실패:', error);
        }
      });
    }
  }

  function editBoardDetails(board) {
    const newBoardName = prompt("Enter the new name of the board:", board.name);
    const newBoardExplanation = prompt("Enter the new explanation of the board:", board.explanation);
    const accessToken = localStorage.getItem('AccessToken');
    if (newBoardName && newBoardExplanation) {
      $.ajax({
        type: 'PATCH',
        url: `/boards/${board.id}`,
        headers: {
          'AccessToken': `${accessToken}`
        },
        data: JSON.stringify({
          name: newBoardName,
          explanation: newBoardExplanation
        }),
        contentType: 'application/json',
        success: function (response) {
          console.log('보드 정보 업데이트 성공:', response);
          fetchBoardDetails(board.id, {...board, name: newBoardName, explanation: newBoardExplanation});

        },
        error: function (xhr, status, error) {
          console.error('보드 정보 업데이트 실패:', error);
        }
      });
    }
  }

  function deleteBoard(boardId) {
    const accessToken = localStorage.getItem('AccessToken');
    $.ajax({
      crossOrigin: true,
      type: 'DELETE',
      url: `/boards/${boardId}`,
      headers: {
        'AccessToken': `${accessToken}`,
        'Content-Type': 'application/json'
      },
      success: function (response) {
        console.log('보드 삭제 성공:', response);
        fetchUserBoards();
      },
      error: function (xhr, status, error) {
        console.error('보드 삭제 실패:', error);
      }
    });
  }

  function editColumn(columns, board, columnId) {
    const newColumnName = prompt("Enter the name of the new column:");
    const accessToken = localStorage.getItem('AccessToken');
    if (newColumnName) {
      $.ajax({
        type: 'PATCH',
        url: `/${board.id}/column/${columnId}`,
        headers: {
          'AccessToken': `${accessToken}`
        },
        data: JSON.stringify({
          name: newColumnName
        }),
        contentType: 'application/json',
        success: function (response) {
          console.log('칼럼 수정 성공:', response);
          // 새로운 칼럼을 기존의 columns 배열에 추가
          columns.push(response.data);
          // 전체 컬럼을 다시 로드
          fetchBoardDetails(board.id, board);
        },
        error: function (xhr, status, error) {
          console.error('칼럼 수정 실패:', error);
        }
      });
    }
  }

  $(document).ready(function () {
    fetchUserBoards();

    $('#add-board-btn').on('click', function () {
      addNewBoard();
    });
  });
});
