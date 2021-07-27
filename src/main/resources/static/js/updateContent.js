$('.content-area').keyup(function (e){
    var content = $(this).val();
    $('#counter').html("("+content.length+" / 최대 2500자)");

    if (content.length > 2500){
        alert("최대 2500자까지 입력 가능합니다.");
        $(this).val(content.substring(0, 2500));
        $('#counter').html("(2500 / 최대 2500자)");
    }
});

function updateContent(hostId, contentId) {
  let title = $('#title').val();
  let description = $('#description').val();
  let contributors = $('#contributors').val();
  let skillstacks = $('#skillstacks').val();
  let startedAt = $('#startedAt').val();
  let endAt = $('#endAt').val();
  let contents = $('#contents').val();
  let links = $('#links').val();

  $.ajax({
    url: '/space/updatecontent/' + contentId,
    method: 'PUT',
    data: {"hostId": hostId, "title": title, "description": description, "contributors":contributors,
  "skillstacks" : skillstacks, "startedAt" : startedAt, "endAt" : endAt, "contents": contents, "links": links},

    error:function(request,status,error){
    alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
}).done(function(data,textStatus,xhr) {
  if(xhr.status==200) {
    alert('변경 완료되었습니다.');
  }

  location.href='/space';
})
}

function deleteContent(contentId) {
  $.ajax({
    url: '/space/deletecontent' + contentId,
    method: 'DELETE',

    error:function(request,status,error){
    alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  }.done(function(data,textStatus,xhr) {
    if(xhr.status == 200) {
      alert('삭제 완료되었습니다.');
    }

    location.href='/space';
  })
  })
}
