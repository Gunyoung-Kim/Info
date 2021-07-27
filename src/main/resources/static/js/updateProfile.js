function withdraw(email) {
  $.ajax({
    url: '/withdraw',
    method: 'DELETE',
    data: {"email": email}
  })
}

function updateProfile() {
  let email = $('#email').val();
  let firstName = $('#firstName').val();
  let lastName = $('#lastName').val();
  let description = $('#description').val();
  let github = $('#github').val();
  let instagram = $('#instagram').val();
  let tweeter = $('#tweeter').val();
  let facebook = $('#facebook').val();

  $.ajax({
      url: '/space/updateprofile',
      method: 'PUT',
      data: {"email": email, "firstName": firstName, "lastName": lastName, "description": description, "github": github,
    "instagram": instagram, "tweeter": tweeter, "facebook": facebook},

      error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      }
  }).done(function(data,textStatus,xhr) {
    if(xhr.status == 200) {
      alert('프로필 변경 되었습니다.');
    }

    location.href = '/space/updateprofile'
  })
}
