<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>图书列表</title>
</head>
<body >
<h1>雷猴啊</h1>
<ul>
	<#list booklist as book>
	    <li>${book.name}:${book.bookId}:${book.number}</li>
	</#list>
</ul>
</body>
</html>