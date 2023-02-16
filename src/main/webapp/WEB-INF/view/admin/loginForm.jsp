<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <form action="/login" method="post">
                        <div class="form-group">
                            <label for="username">아이디</label>
                            <input type="text" class="form-control" id="username" name="username" placeholder="아이디를 입력하세요">
                        </div>
                        <div class="form-group mb-2">
                            <label for="password">비밀번호</label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요">
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">로그인</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>