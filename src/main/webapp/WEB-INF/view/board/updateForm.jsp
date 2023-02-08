<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>

    <div class="container my-3">
        <form id="updateBox">
            <div class="form-group">
                <input type="text" class="form-control" placeholder="Enter title" name="title" id="title" value="${board.title}">
            </div>

            <div class="form-group">
                <textarea class="form-control summernote" rows="5" id="content" name="content">${board.content}</textarea>
            </div>
            <button type="button" class="btn btn-primary" onClick="updateById(${board.id})">글수정완료</button>
        </form>

    </div>

    <script>
        $('.summernote').summernote({
            tabsize: 2,
            height: 400
        });

        
    </script>

    <script>
        function updateById(id) {
            let updateData = {
                title: $('#title').val(),
                content: $('#content').val()
            }

            $.ajax({
                type: "put",
                url: "/board/" + id,
                data: JSON.stringify(updateData),
                headers: {
                    "Content-type": "application/json; charset=UTF-8"
                },
                dataType: "json" // 응답의 MIME타입으로 유추함(Default)
            })
            .done((res) => {
                alert(res.msg);
                location.href="/board/" + id
            })
            .fail((err) => {
                alert(err.responseJSON.msg);
            })
        }
    </script>

<%@ include file="../layout/footer.jsp" %>