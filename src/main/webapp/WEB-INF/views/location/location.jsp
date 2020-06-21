<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>영화관 위치정보</title>

	<link rel="stylesheet" href="${ pageContext.request.contextPath }/resources/css/user.css">
	
	<style>
		/* -------기존------ */
		body{
           background-image: url(${ pageContext.request.contextPath }/resources/img/footer_bg.png);
           background-repeat: repeat; 
        }
	   .field_form {width:80%; height:620px; position:relative; margin:50px auto;}
       .field_form > ul{width:100%; height:580; position:absolute; background:rgb(119, 119, 119, 0.5); border-radius:40px; }
       .field_form > ul > li {list-style:none; margin-top:30px; }
       .field_form h3 {width:300px; height:50px; background:rgb(255, 255, 255, 0.3); color:white; border-radius:20px; text-align:center; font-family: sans-serif; letter-spacing:1px; font-size:18px; font-weight:1.5em; line-height:50px; margin-bottom:20px;}
       .li1{float:left; width:55%; height:500px; margin-left:30px;}
       .li2{float:left; width:40%; height:500px; margin-left:30px; display:block;}
       .input_wrapper {float:left;}
       
       .find_way ul{ list-style:none; }
       .find_way p{width:90%; margin-left:10px; color:white; font-size:15px; font-family: sans-serif; letter-spacing:1px;}
       .find_way strong{ width:90%; color:white; margin-left:10px; font-size:17px; text-decoration:underline; letter-spacing:2px; line-height:50px; font-family: sans-serif;}
   
       #footer{ width:100%; height:200px; margin-top:920px;}
       /* ----------------------- */
       
       /* 2020 06 21 추가 */
        .overlaybox {position:relative;width:360px;height:350px;background:url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/box_movie.png') no-repeat;padding:15px 10px;}
		.overlaybox div, ul {overflow:hidden;margin:0;padding:0;}
		.overlaybox li {list-style: none;}
		.overlaybox .boxtitle {color:#fff;font-size:16px;font-weight:bold;background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png') no-repeat right 120px center;margin-bottom:8px;}
		.overlaybox .first {position:relative;width:247px;height:136px;background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/thumb.png') no-repeat;margin-bottom:8px;}
		.first .title1 {color:#fff;font-weight:bold;}
		.first .triangle {position:absolute;width:48px;height:48px;top:0;left:0;background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/triangle.png') no-repeat; padding:6px;font-size:18px;}
		.first .title1 {position:absolute;width:100%;bottom:0;background:rgba(0,0,0,0.4);padding:7px 15px;font-size:14px;}
		.overlaybox ul {width:247px;}
		.overlaybox li {position:relative;margin-bottom:2px;background:#2b2d36;padding:5px 10px;color:#aaabaf;line-height: 1;}
		.overlaybox li span {display:inline-block;}
		.overlaybox li .number {font-size:16px;font-weight:bold;}
		.overlaybox li .title {font-size:13px;}
		.overlaybox ul .arrow {position:absolute;margin-top:8px;right:25px;width:5px;height:3px;background:url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/updown.png') no-repeat;} 
		.overlaybox li .up {background-position:0 -40px;}
		.overlaybox li .down {background-position:0 -60px;}
		.overlaybox li .count {position:absolute;margin-top:5px;right:15px;font-size:10px;}
		.overlaybox li:hover {color:#fff;background:#d24545;}
		.overlaybox li:hover .up {background-position:0 0px;}
		.overlaybox li:hover .down {background-position:0 -20px;}
       /* -------------- */
       
       
	</style>
	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/httpRequest.js"></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/resources/js/needDate.js"></script>
	<script type="text/javascript">
		window.onload=function(){
			customOverlay_boxOff_list();
			var title0;
			var title1;
			var title2;
			var title3;
			var title4;

	     };
		var today = loadDate()-1;//1일에 문제될수있음
		
		function customOverlay_boxOff_list(){
	        //192.168.1.101:9090/vs/list.do
	        var url ='http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json';
	        var param = 'key=a7c6bfb2e16d4d1ae14730f90bc6726a&targetDt='+today+'&itemPerPage=5';
	        sendRequest( url, param, result_customOverlay_boxOff_list, "GET" );   
	     }
		
		function result_customOverlay_boxOff_list(){
	         if( xhr.readyState == 4 && xhr.status == 200 ){
	            var data = xhr.responseText;
	            var json = eval("["+data+"]");
	            for(var i=0 ; i<json[0].boxOfficeResult.dailyBoxOfficeList.length ; i++){
	            	document.getElementsByClassName("title"+i).innerText = json[0].boxOfficeResult.dailyBoxOfficeList[i].movieNm; //영화 제목
	                //json[0].boxOfficeResult.dailyBoxOfficeList[i].salesShare+" %"; //예매율
	                console.log(i+"/"+json[0].boxOfficeResult.dailyBoxOfficeList[i].movieNm);
	            }
	            
	         }
	         
	      }
	</script>
	
</head>
<body>
	
	<div id="moviewrap">
		<jsp:include page="../header.jsp"/>
		<div id="container">
			<div class="field_form">
				<ul>
					<li class="li1">
						<h3>NIGHT CINEMA 위치</h3>
						<div id="map" style="width: 800px; height: 400px;"></div>
						
						<!-- 카카오지도 API -->
						<script type="text/javascript"
							src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2c495537dfdb95fa59c9af35372bf3ca"></script>
						<script>
							var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
							mapOption = {
								center : new kakao.maps.LatLng(37.554480, 126.936092), // 지도의 중심좌표
								level : 2 // 지도의 확대 레벨
							};
					
							var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
							// 마커가 표시될 위치입니다 
							var markerPosition  = new kakao.maps.LatLng(37.554150, 126.935714); 
							
							/* --------------------2020 06 21 추가--------------------- */
							var position = new kakao.maps.LatLng(37.554150, 126.935714);
							var content = '<div class="overlaybox">' +
										    '    <div class="boxtitle">금일 영화순위</div>' +
										    '    <div class="first">' +
										    '        <div class="triangle_text">1</div>' +
										    '        <div class="title0"></div>' +
										    '    </div>' +
										    '    <ul>' +
										    '        <li class="up">' +
										    '            <span class="number">2</span>' +
										    '            <span class="title1">22</span>' +
										    '            <span class="arrow up"></span>' +
										    '            <span class="count">2</span>' +
										    '        </li>' +
										    '        <li>' +
										    '            <span class="number">3</span>' +
										    '            <span class="title2">33</span>' +
										    '            <span class="arrow up"></span>' +
										    '            <span class="count">6</span>' +
										    '        </li>' +
										    '        <li>' +
										    '            <span class="number">4</span>' +
										    '            <span class="title3">44</span>' +
										    '            <span class="arrow up"></span>' +
										    '            <span class="count">3</span>' +
										    '        </li>' +
										    '        <li>' +
										    '            <span class="number">5</span>' +
										    '            <span class="title4">55</span>' +
										    '            <span class="arrow down"></span>' +
										    '            <span class="count">1</span>' +
										    '        </li>' +
										    '    </ul>' +
										    '</div>';
										    
										    
						    var customOverlay = new kakao.maps.CustomOverlay({
						        position: position,
						        content: content,
						        xAnchor: 0.3,
						        yAnchor: 0.91
						    });
						    customOverlay.setMap(map);
							/* ----------------------------------------------------- */
							
							
							// 마커를 생성합니다
							var marker = new kakao.maps.Marker({
							    position: markerPosition
							});
					
							// 마커가 지도 위에 표시되도록 설정합니다
							marker.setMap(map);
					
							var iwContent = '<div style="padding:5px; margin-left:10px;"><i>NIGHT CINEMA</i></div>';
							    iwPosition = new kakao.maps.LatLng(37.554150, 126.935714); //인포윈도우 표시 위치입니다
					
							// 인포윈도우를 생성합니다
							var infowindow = new kakao.maps.InfoWindow({
							    position : iwPosition, 
							    content : iwContent 
							});
							  
							// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
							infowindow.open(map, marker);
							
							
							
							
						</script>
					</li>
					
					<li class="li2">
						<h3>오시는 길</h3>
						
						<div class="find_way">
							<ul>
								<li><strong>주소 :</strong></li>
								<li style="margin-bottom:10px;"><p>서울특별시 마포구 서강로 136 아이비타워 2층,3층(노고산동)</p></li>
								
								<li><strong>대표번호 :</strong></li>
								<li style="margin-bottom:10px;"><p>02-313-7300</p></li>
								
								<li><strong>지하철 이용 시 :</strong></li>
								<li style="margin-bottom:10px;"><p>신촌역 2호선 7번 출구</p></li>
								
								<li><strong>버스 이용시 :</strong></li>
								<li>
									<ul>
										<li><p>BLUE : 163, 170, 171, 172, 271, 371, 472, 602, 603, 705</p></li>
										<li><p>GREEN : 5711, 5712, 5713, 7011, 7012, 7014, 7015, 7611, 마포07, 마포10, 서대문05</p></li>
									</ul>
								</li>
							</ul>
						</div>					
					</li>
				</ul>
			</div>
		</div>
	
		<!-- footer -->
		<div id="footer" style="margin-top:900px;">
			<div class="f_bg">
				<img
					src="${ pageContext.request.contextPath }/resources/img/footer_bg.png">
			</div>
			<div class="f_txt">
				<p class="f_logo">
					<img src="${ pageContext.request.contextPath }/resources/img/logo_test.png">
				</p>
				
				<address>서울특별시 마포구 서강로 136 아이비티워 2층,3층</address>
				<p class="team1">2조 Spring Project Movie</p>
				<p class="team2">민형, 성수, 우성, 선영, 원경, 유진</p>
			</div>
		</div>
		<!-- footer 끝 -->
	</div>
</body>
</html>