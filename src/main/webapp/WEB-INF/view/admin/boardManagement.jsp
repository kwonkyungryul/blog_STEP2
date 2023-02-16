<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>

<div class="container-fluid mt-5">
    <div class="row">
        <!-- 왼쪽 카테고리 -->
        <div class="col-md-3">
            <div class="list-group">
                <a href="/admin/userManagement" class="list-group-item list-group-item-action">회원 관리</a>
                <a href="/admin/boardManagement" class="list-group-item list-group-item-action active">게시글 관리</a>
                <a href="/admin/replyManagement" class="list-group-item list-group-item-action">댓글 관리</a>
            </div>
        </div>
        <!-- 오른쪽 테이블 -->
    <div class="col-md-9">
        <table class="table table-bordered">
          <thead>
            <tr class="text-center">
              <th>번호</th>
              <th>제목</th>
              <th>작성자</th>
              <th>작성일</th>
              <th>비고</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${dtos}" var="dto">
              <tr id="list-${dto.id}" class="text-center">
                  <td>${dto.id}</td>
                  <td>${dto.title}</td>
                  <td>${dto.username}</td>
                  <td>${dto.createdAt}</td>
                  <td><button type="button" class="btn btn-danger btn-sm" onclick="deleteById(${dto.id})">삭제</button></td> <!-- 삭제 버튼 추가 -->
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <div class="form-group">
          <form action="/admin/boardManagement" method="get">
            <div class="input-group">
              <select name="searchOpt" id="" class="rounded me-2 border-">
                <option value="all" <c:if test="${searchOpt eq 'all'}">selected</c:if>>전체</option>
                <option value="title" <c:if test="${searchOpt eq 'title'}">selected</c:if>>제목</option>
                <option value="username" <c:if test="${searchOpt eq 'username'}">selected</c:if>>작성자</option>
              </select>
              <input type="text" class="form-control me-2 rounded" name="words" placeholder="검색어를 입력하세요">
              <div class="input-group-append">
                <button class="btn btn-secondary" type="submit">검색</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
</div>

<script>
    function deleteById(id) {
          $.ajax({
              type: "delete",
              url: "/board/" + id,
              dataType: "json"
          })
          .done((res)=>{ // 20X 일 때
              alert(res.msg);
              $('#list-'+id).remove();
          })
          .fail((err)=>{ // 40X, 50X 일 때
              alert(err.responseJSON.msg);
          })
      }
</script>


<%@ include file="../layout/footer.jsp" %>