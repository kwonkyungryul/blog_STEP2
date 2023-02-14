<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %>

<style>
    .container {
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    h2 {
        margin-top: 2rem;
    }
    form {
        width: 50%;
        margin-top: 2rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        /* border: 1px solid gray; */
        padding: 1rem;
        border-radius: 10px;
    }
    .form-group {
        margin-bottom: 1rem;
        text-align: center;
    }
    .form-group img {
        width: 290px;
        height: 270px;
        border-radius: 50%;
        margin-bottom: 1rem;
        border: 1px solid gray;
    }
    .btn {
        margin-top: 1rem;
        width: 20%;
    }
</style>

<div class="container my-3">
    <h2 class="text-center">프로필 사진 변경 페이지</h2>
    <form id="profileForm" action="/user/profileUpdate" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <img src="${user.profile == null ? '/images/dora.png' : user.profile}" alt="Current Photo" class="img-fluid" id="imagePreview">
        </div>
        <div class="form-group">
            <input type="file" class="form-control" id="profile" name="profile" onchange="chooseImage(this)">
        </div>
        <button type="submit" class="btn btn-primary">사진변경</button>
    </form>
</div>

<script>
    // ajax
    function updateImage() {
        let profileForm = $(`#profileForm`)[0];
        let formData = new FormData(profileForm);

        $.ajax({
                type: "put",
                url: "/user/profileUpdate",
                data: formData,
                contentType: false, // 필수 : x-www-form-urlencoded로 파싱되는 것을 방지
                processType: false, // 필수 : contentType을 false로 줬을 때 QueryString 자동 설정 됨. 해제.
                enctype: "multipart/form-data",
                dataType: "json" // 응답의 MIME타입으로 유추함(Default)
            })
            .done((res) => {
                alert(res.msg);
                location.href="/";
            })
            .fail((err) => {
                alert(err.responseJSON.msg);
            })
    }

    function chooseImage(obj) {
        // console.log(obj);
        // console.log(obj.files);
        let f = obj.files[0];
        console.log(f);

        if (f.type.match("image.*")) { // MIME 타입
            alert(`이미지를 등록해야 합니다`);
            return;
        }

        let reader = new FileReader(); // 자바에서는 BufferedReader
        
        reader.readAsDataURL(f); // 파일을 읽음 // I/O는 오래 걸린다. I/O는 항상 CPU를 느리게 만든다(I/O하는 동안 기다리기 때문). // HDD는 물리적으로 데이터를 기록(돌을 긁어서 기록을 남기듯이 상처를 냄) - I/O, RAM은 전류로 데이터를 기록(전류적으로  기록되어 있는 것들은 Access가 엄청 빠르다.) - Cathe
        // 이벤트가 안 일어나는 것들은 callStack 공간에 다 넣는다(바로바로 실행되는 것들(HTML, CSS, chooseImage....).
        // reader.readAsDataURL(f)가 실행될 때 이벤트로 등록한다(오래걸리기 때문).
        // 이벤트는 오래걸리니까 뒤로하고 callStack부터 실행한다. 다 실행한 후 이벤트루프로 가서 fending이 끝났는지 확인하고 끝나지 않았다면 이벤트 루프를 한바퀴 돌고 다시 확인하고 fending이 끝났다면 callStack에 등록한다. 내부적으로 구현되어 있는 callback함수를 호출한다.
        // 싱글스레드이기 때문
        reader.onload = (e)=>{ // FileReader의 callback함수 // 콜스택이 다 비워지고, 이벤트 루프로 가서 readAsDataURL 이벤트가 끝나면 callback시켜주는 함수 // 자바에서는 안에 interface를 등록시켜준다.(왜? 함수를 등록 못하기 때문)
            document.querySelector(`#imagePreview`).setAttribute("src", e.target.result);
            console.log(e); // context의 정보
        };
    }
</script>

<%@ include file="../layout/footer.jsp" %>