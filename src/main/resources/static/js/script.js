document.addEventListener('DOMContentLoaded', (event) => {
  const board = document.getElementById('board');
  const addListButton = document.getElementById('add-list');

  let draggedCard = null;
  let draggedList = null;

  addListButton.addEventListener('click', () => {
    const list = createList();
    board.appendChild(list);
  });

  function createList() {
    const list = document.createElement('div');
    list.className = 'list';
    list.draggable = true;

    const listTitle = document.createElement('h3');
    listTitle.className = 'list-title';
    listTitle.contentEditable = true;
    listTitle.innerText = 'New List';

    const deleteListButton = document.createElement('button');
    deleteListButton.className = 'delete-list';
    deleteListButton.innerHTML = '&times;';
    deleteListButton.addEventListener('click', () => {
      board.removeChild(list);
    });

    const addCardButton = document.createElement('button');
    addCardButton.className = 'add-card';
    addCardButton.innerText = 'Add Card';
    addCardButton.addEventListener('click', () => {
      const card = createCard();
      list.insertBefore(card, addCardButton);
    });

    listTitle.appendChild(deleteListButton);
    list.appendChild(listTitle);
    list.appendChild(addCardButton);

    list.addEventListener('dragstart', (e) => {
      draggedList = list;
      setTimeout(() => {
        list.style.display = 'none';
      }, 0);
    });

    list.addEventListener('dragend', (e) => {
      setTimeout(() => {
        list.style.display = 'block';
        draggedList = null;
      }, 0);
    });

    list.addEventListener('dragover', (e) => {
      e.preventDefault();
    });

    list.addEventListener('dragenter', (e) => {
      e.preventDefault();
      list.style.backgroundColor = 'rgba(0, 0, 0, 0.1)';
    });

    list.addEventListener('dragleave', (e) => {
      list.style.backgroundColor = 'transparent';
    });

    list.addEventListener('drop', (e) => {
      list.style.backgroundColor = 'transparent';
      if (draggedCard) {
        list.insertBefore(draggedCard, addCardButton);
      } else if (draggedList) {
        board.insertBefore(draggedList, list);
      }
    });

    return list;
  }

  function createCard() {
    const card = document.createElement('div');
    card.className = 'card';
    card.contentEditable = true;
    card.innerText = 'New Task';
    card.draggable = true;

    const deleteCardButton = document.createElement('button');
    deleteCardButton.className = 'delete-card';
    deleteCardButton.innerHTML = '&times;';
    deleteCardButton.addEventListener('click', () => {
      card.parentElement.removeChild(card);
    });

    card.appendChild(deleteCardButton);

    card.addEventListener('dragstart', (e) => {
      draggedCard = card;
      setTimeout(() => {
        card.style.display = 'none';
      }, 0);
    });

    card.addEventListener('dragend', (e) => {
      setTimeout(() => {
        card.style.display = 'block';
        draggedCard = null;
      }, 0);
    });

    return card;
  }

  // Initialize with one list
  const initialList = createList();
  board.appendChild(initialList);
});
