// card.js
document.addEventListener('DOMContentLoaded', function() {
  // 카드를 클릭했을 때 모달을 표시하는 함수
  function showCardModal(card) {
    $('#modal-title').text(card.title);
    $('#modal-contents').text(card.contents);
    $('#modal-author').text(card.author);
    $('#card-modal').show();
  }

  // 카드 클릭 이벤트 추가
  $(document).on('click', '.board-card', function() {
    const cardId = $(this).data('card-id');

    // 카드 정보를 서버에서 가져와서 모달에 표시
    $.ajax({
      type: 'GET',
      url: `/cards/${cardId}`,
      headers: {
        'Content-Type': 'application/json'
      },
      success: function(response) {
        showCardModal(response.data);
      },
      error: function(xhr, status, error) {
        console.error('Failed to fetch card information:', error);
      }
    });
  });

  // 모달 닫기 이벤트 추가
  $('.close-btn').on('click', function() {
    $('#card-modal').hide();
  });

  $(window).on('click', function(event) {
    if ($(event.target).hasClass('modal')) {
      $('#card-modal').hide();
    }
  });
});
