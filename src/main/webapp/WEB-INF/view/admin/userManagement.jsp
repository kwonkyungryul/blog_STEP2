<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>

<div class="container-fluid mt-5">
  <div class="row">
    <!-- 왼쪽 카테고리 -->
    <div class="col-md-3">
      <div class="list-group">
        <a href="/admin/userManagement" class="list-group-item list-group-item-action active">회원 관리</a>
        <a href="/admin/boardManagement" class="list-group-item list-group-item-action">게시글 관리</a>
        <a href="/admin/replyManagement" class="list-group-item list-group-item-action">댓글 관리</a>
      </div>
    </div>
    <!-- 오른쪽 테이블 -->
    <div class="col-md-9">
      <!-- 검색 칸 -->
      <table class="table table-bordered">
        <thead>
          <tr class="text-center">
            <th>번호</th>
            <th>아이디</th>
            <th>패스워드</th>
            <th>이메일</th>
            <th>권한</th>
            <th>가입일</th>
            <th>비고</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${userList}" var="user">
            <tr id="list-${user.id}" class="text-center">
              <td>${user.id}</td>
              <td>${user.username}</td>
              <td>${user.password}</td>
              <td>${user.email}</td>
              <td>${user.role}</td>
              <td>${user.createdAt}</td>
              <td><button type="button" class="btn btn-danger btn-sm" onclick="deleteById(${user.id})">삭제</button>
              </td> <!-- 삭제 버튼 추가 -->
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <div class="form-group">
        <form action="/admin/userManagement" method="get">
          <div class="input-group">
            <select name="searchOpt" id="" class="rounded me-2 border-">
              <option value="all" <c:if test="${searchOpt eq 'all'}">selected</c:if>>전체</option>
              <option value="username" <c:if test="${searchOpt eq 'username'}">selected</c:if>>회원아이디</option>
              <option value="email" <c:if test="${searchOpt eq 'email'}">selected</c:if>>이메일</option>
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
          url: "/user/" + id,
          dataType: "json"
        })
          .done((res) => { // 20X 일 때
            alert(res.msg);
            $('#list-' + id).remove();
          })
          .fail((err) => { // 40X, 50X 일 때
            alert(err.responseJSON.msg);
          })
      }
    </script>


    <%@ include file="../layout/footer.jsp" %>