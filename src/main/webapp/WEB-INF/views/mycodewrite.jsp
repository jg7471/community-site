<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 글쓰기</title>

    <%@ include file="/WEB-INF/views/include/static-head.jsp" %>
    <link rel="stylesheet" href="/assets/css/header.css">

    
    <link rel="stylesheet" href="/assets/css/snb.css" >
 
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/reset-css@5.0.1/reset.min.css">


    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css">

   

 <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.ckeditor.com/4.17.2/standard/ckeditor.js"></script>
    <style>
body{
    background-color: #000080;
}

        .form-container {
            width: 80%;
    margin: auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 60px;
    font-size: 18px;
    padding: 5%;
    position: relative;
    top: 170px;
            
        }
        .form-main{
            
    border: 1px solid black;
    border-radius: 60px;
   
    padding: 5%;
        }
        .form-container h1 {
            font-size: 50px;
            font-weight: bold;
            letter-spacing: 10px;
            text-align: center;
            margin-bottom: 20px;
            color: black;
            padding: 40px 0px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-size: 20px;
            color: black;
        }
        input[type="text"],
        textarea {
            font-size: 18px;
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid black;
            border-radius: 8px;
            margin-bottom: 10px;
            
        }
        textarea {
            resize: none;
            height: 200px;
        }
        .buttons {
            display: flex;
            justify-content: flex-end;
            margin-top: 20px;
        }
        button {
            font-size: 20px;
    background: white;
    border: 2px solid #00196b;
        }
       
        button:hover {
            background: #00196b;
            color: #fff;
        }
        button.list-btn:hover {
            background: #00196b;
            color: #fff;
        }
        select{
            font-size: 18px;
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid black;
            border-radius: 8px;
            margin-bottom: 10px;
       
            
        }
.list-btn{
    margin-right: 10px;
}



    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/include/header.jsp" %>
<div id="wrap" class="form-container">
    <h1>My Code</h1>
    <form action="/wel/write" method="post" enctype="multipart/form-data" class="form-main">
        
        <select class="form-select" id="programming" name="programming" aria-label="Default select example">                
            <option value="HTML">HTML</option>
            <option value="CSS">CSS</option>
            <option value="JAVA">JAVA</option>
            <option value="C">C</option>
            <option value="JavaScript">JavaScript</option>
            <option value="Python">Python</option>
            <option value="Other-than">Other-than</option>          
        </select>

        <label for="title">제목</label>
        <input type="text" id="title" name="title" required>
        
        <label for="content">내용</label>
        <textarea id="content" name="content" maxlength="200" required></textarea>
        <div class="buttons">
            <button class="list-btn" type="button" onclick="window.location.href='/wel/myCode'">목록</button>
            <button type="submit">글쓰기</button>
        </div>
    </form>
</div>
<script>
  CKEDITOR.replace('content');

  
        

     

</script>
</body>
</html>


</body>
</html>