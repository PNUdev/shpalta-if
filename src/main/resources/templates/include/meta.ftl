<meta charset="UTF-8">
<meta name="viewport"
      content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<#--    ToDo Update links when deployed-->
<#assign domainName = "http://localhost:8080" />

<#if post??>
    <meta property="og:title" content="${post.title}">
    <meta property="og:image" content="${post.pictureUrl}">
    <meta property="og:url" content="${domainName}/posts/${post.id}">
<#else>
    <meta property="og:title" content="Шпальта ІФ">
    <meta property="og:image" content="${domainName}/images/shpalta_logo.png">
    <meta property="og:url" content="${domainName}/">
</#if>

<meta property="og:description" content="Головні події Івано-Франківська та світу. Читайте на Шпальта ІФ!">
<meta property="og:site_name" content="SHPALTA IF, Inc">

<meta name="twitter:card" content="summary_large_image">
<meta name="twitter:image:alt" content="Зображення не знайдено">
<meta name="description" content="Головні події Івано-Франківська та світу. Читайте на Шпальта ІФ!">