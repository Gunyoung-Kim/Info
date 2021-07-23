var inputNum = 1;

function addKeyUP() {
  $('.content-area').keyup(function (e){
    var content = $(this).val();
    $('#counter').html("("+content.length+" / 최대 2500자)");

    if (content.length > 2500){
        alert("최대 2500자까지 입력 가능합니다.");
        $(this).val(content.substring(0, 2500));
        $('#counter').html("(2500 / 최대 2500자)");
    }
  });
}

function addLinkInput() {
  var newInput = $(`<div id="link${inputNum}"></div>`);

  var tagInput = $(`<input type="text">`,{id: `tagInput${inputNum}`});
  var urlInput = $(`<input type="text">`,{id: `urlInput${inputNum}`});

  var deleteBtn = $(`<button type="button" onclick="deleteSelect(${inputNum})" class="btn">삭제</button>`);
  inputNum++;

  newInput.append(tagInput);
  newInput.append(urlInput);
  newInput.append(deleteBtn);

  $('#linkInput').append(newInput);
}

function addContent(email) {
  var dto = new Object();
  dto.title = $('#title').val();
  dto.description = $('#description').val();
  dto.contributors = $('#contributors').val();
  dto.skillstacks = $('#skillstacks').val();
  dto.startedAt = $('#startedAt').val();
  dto.endAt = $('#endAt').val();
  dto.contents = $('#contents').val();

  var links = new Array();

  for(let i=1; i<inputNum;i++) {
    let tag = $(`#tagInput${i}`).val();
    let url = $(`#urlInput${i}`).val();

    if(tag != "" && url != "") {
      var link = new Object();
      link.description = tag;
      link.url = url;

      links.push(link);
    }
  }

  dto.links = links;

  $.ajax({
    url: '/space/makecontent/' + email,
    method: 'POST',
    data: dto,
    error:function(request,status,error){
    alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  })
}

$(document).ready(function() {
  addKeyUP();
})
