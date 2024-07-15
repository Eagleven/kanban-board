// 보드 및 칼럼 관리
document.addEventListener('DOMContentLoaded', function () {
  function fetchUserBoards() {
    // 로컬 스토리지에서 accessToken 가져오기
    const accessToken = localStorage.getItem('accessToken');
    console.log(accessToken);

    // AJAX 요청을 통해 보드 리스트 가져오기
    $.ajax({
      crossOrigin : true,
      type: 'GET',
      url: '/boards?page=1',
      headers: {
        'AccessToken': `${accessToken}`,
        'Content-Type': 'application/json'
      },
      success: function(response) {
        // 서버에서 받은 데이터로 보드 리스트 표시
        const boards = response.data; // 응답 데이터의 형식에 따라 수정
        displayUserBoards(boards);

        // 첫 번째 보드를 자동으로 선택
        if (boards.length > 0) {
          const firstBoardId = boards[0].id;
          fetchBoardDetails(firstBoardId);
        }
      },
      error: function(xhr, status, error) {
        console.error('보드 리스트를 가져오는 데 실패했습니다:', error);
      }
    });
  }

// 보드 상세 정보 조회 함수
  function fetchBoardDetails(boardId) {
    // 로컬 스토리지에서 accessToken 가져오기
    const accessToken = localStorage.getItem('accessToken');

    // AJAX 요청을 통해 보드 상세 정보 가져오기
    $.ajax({
      type: 'GET',
      url: `/boards/${boardId}`,
      headers: {
        'AccessToken': `${accessToken}`,
        'Content-Type': 'application/json'
      },
      success: function(response) {
        const boardDetails = response.data; // 응답 데이터의 형식에 따라 수정
        displayBoardDetails(boardDetails);
      },
      error: function(xhr, status, error) {
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
                </div>
            `);
      boardItem.on('click', function(event) {
        if (!$(event.target).hasClass('trash-icon')) {
          const boardId = $(this).data('id');
          displayBoardDetails(boardId);
        }
      });
      boardItem.find('.trash-icon').on('click', function() {
        const boardId = $(this).data('board-id');
        if (confirm('삭제하는 경우 연결된 데이터가 전부 삭제됩니다. 정말 삭제하시겠습니까?')) {
          deleteBoard(boardId);
        }
      });
      boardsList.append(boardItem);
    });
  }

  function displayBoardDetails(board) {
    const selectedBoard = $('#selected-board');
    selectedBoard.empty();

    const boardContent = `
            <div class="board-details">
                <h2>${board.name} <i class="edit icon edit-icon" id="edit-board-icon"></i></h2>
                <p>${board.explanation}</p>
                <div class="board-columns" id="board-columns">
                    ${board.columns.map(column => `
                        <div class="board-column" data-column-id="${column.id}">
                            <h3 class="list-title">${column.name}<button class="delete-list">&times;</button></h3>
                            <div class="board-cards">
                                ${column.cards.map(card => `
                                    <div class="board-card card" data-board-id="${board.id}" data-column-id="${column.id}" data-card-id="${card.id}">
                                        <p>${card.title}</p><button class="delete-card">&times;</button>
                                    </div>
                                `).join('')}
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

    // 칼럼 드래그 앤 드롭 설정
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

      // 위치 변경을 서버에 저장
      updateColumnPosition(columnId, newPosition);
    });

    // 카드 드래그 앤 드롭 설정
    const cardContainers = Array.from(document.getElementsByClassName('board-cards'));
    const cardDrake = dragula(cardContainers, {
      moves: function (el, container, handle) {
        return handle.classList.contains('board-card') || handle.tagName === 'P';
      }
    });
    cardDrake.on('drop', (el, target, source, sibling) => {
      const cardId = el.getAttribute('data-card-id');
      const newColumnId = target.parentNode.getAttribute('data-column-id');
      const newPosition = Array.prototype.indexOf.call(target.children, el);

      // 위치 변경을 서버에 저장
      updateCardPosition(cardId, newColumnId, newPosition);
    });

    // 카드 클릭 이벤트 추가
    $('.board-card').on('click', function() {
      const boardId = $(this).data('board-id');
      const columnId = $(this).data('column-id');
      window.location.href = `/card.html?boardId=${boardId}&columnId=${columnId}`;
    });

    // 새로운 칼럼 추가 이벤트 추가
    $('#add-column').on('click', function() {
      addNewColumn(board.id);
    });

    // 보드 편집 아이콘 클릭 이벤트 추가
    $('#edit-board-icon').on('click', function() {
      editBoardDetails(board);
    });

    // 카드 삭제 이벤트 추가
    $('.delete-card').on('click', function() {
      $(this).parent().remove();
    });

    // 리스트 삭제 이벤트 추가
    $('.delete-list').on('click', function() {
      $(this).closest('.board-column').remove();
    });

    // 새로운 카드 추가 이벤트 추가
    $('.add-card').on('click', function() {
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
      success: function(response) {
        console.log('카드 위치 업데이트 성공:', response);
      },
      error: function(xhr, status, error) {
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
      success: function(response) {
        console.log('칼럼 위치 업데이트 성공:', response);
      },
      error: function(xhr, status, error) {
        console.error('칼럼 위치 업데이트 실패:', error);
      }
    });
  }

  function addNewColumn(boardId) {
    const newColumnName = prompt("Enter the name of the new column:");
    if (newColumnName) {
      $.ajax({
        type: 'POST',
        url: '/column',
        data: JSON.stringify({
          boardId: boardId,
          name: newColumnName
        }),
        contentType: 'application/json',
        success: function(response) {
          console.log('새 칼럼 추가 성공:', response);
          // 새로운 칼럼을 추가한 후 보드 세부 정보를 다시 불러와서 업데이트
          displayBoardDetails(dummyBoardDetails[boardId]);
        },
        error: function(xhr, status, error) {
          console.error('새 칼럼 추가 실패:', error);
        }
      });
    }
  }

  function addNewCard(columnId, columnElement) {
    const newCardTitle = prompt("Enter the title of the new card:");
    if (newCardTitle) {
      $.ajax({
        type: 'POST',
        url: `/column/${columnId}`,
        data: JSON.stringify({
          title: newCardTitle
        }),
        contentType: 'application/json',
        success: function(response) {
          console.log('새 카드 추가 성공:', response);
          const card = $(`
                        <div class="board-card card" data-card-id="${response.id}">
                            <p>${newCardTitle}</p><button class="delete-card">&times;</button>
                        </div>
                    `);
          card.insertBefore(columnElement.find('.add-card'));
          card.find('.delete-card').on('click', function() {
            $(this).parent().remove();
          });
        },
        error: function(xhr, status, error) {
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
      // 로컬 스토리지에서 accessToken 가져오기
      const accessToken = localStorage.getItem('accessToken');

      $.ajax({
        type: 'POST',
        url: '/api/boards',
        headers: {
          'AccessToken': `${accessToken}`, // Authorization 헤더에 토큰 추가
          'Content-Type': 'application/json'
        },
        data: JSON.stringify({
          name: newBoardName,
          explanation: newBoardExplanation
        }),
        success: function(response) {
          console.log('새 보드 추가 성공:', response);
          // 새로운 보드를 추가한 후 보드 목록을 다시 불러와서 업데이트
          fetchUserBoards();
        },
        error: function(xhr, status, error) {
          console.error('새 보드 추가 실패:', error);
        }
      });
    }
  }


  function editBoardDetails(board) {
    const newBoardName = prompt("Enter the new name of the board:", board.name);
    const newBoardExplanation = prompt("Enter the new explanation of the board:", board.explanation);
    if (newBoardName && newBoardExplanation) {
      $.ajax({
        type: 'PATCH',
        url: `/board/${board.id}`,
        data: JSON.stringify({
          name: newBoardName,
          explanation: newBoardExplanation
        }),
        contentType: 'application/json',
        success: function(response) {
          console.log('보드 정보 업데이트 성공:', response);
          // 보드 정보를 업데이트한 후 보드 세부 정보를 다시 불러와서 업데이트
          displayBoardDetails({
            ...board,
            name: newBoardName,
            explanation: newBoardExplanation,
          });
        },
        error: function(xhr, status, error) {
          console.error('보드 정보 업데이트 실패:', error);
        }
      });
    }
  }

  function deleteBoard(boardId) {
    $.ajax({
      type: 'DELETE',
      url: `/board/${boardId}`,
      success: function(response) {
        console.log('보드 삭제 성공:', response);
        // 보드를 삭제한 후 보드 목록을 다시 불러와서 업데이트
        fetchUserBoards();
      },
      error: function(xhr, status, error) {
        console.error('보드 삭제 실패:', error);
      }
    });
  }

  $(document).ready(function() {
    fetchUserBoards();

    // 새로운 보드 추가 이벤트 추가
    $('#add-board-btn').on('click', function() {
      addNewBoard();
    });
  });
});