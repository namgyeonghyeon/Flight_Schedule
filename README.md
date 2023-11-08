프로젝트명: flight schedule

프로젝트 설명: 공공데이터의 인천국제공 api 파싱하여 현재 인천공항의 원하는 데이터를 불러오는 프로젝트

사용 언어 : java


[화면 구성]

![image](https://github.com/namgyeonghyeon/Flight_Schedule/assets/129054045/2fd24466-698f-4ac0-84d4-61c6a2ebcfe6)

![image](https://github.com/namgyeonghyeon/Flight_Schedule/assets/129054045/5f1d5870-ef64-431b-bc4f-335178e3a915)

![image](https://github.com/namgyeonghyeon/Flight_Schedule/assets/129054045/f93ec6fd-7570-46ec-92b0-186719a8d0c1)

![image](https://github.com/namgyeonghyeon/Flight_Schedule/assets/129054045/fcd9e415-11db-4b35-9bea-6bbe5b00f733)

풍속 1.0이상 선택

![image](https://github.com/namgyeonghyeon/Flight_Schedule/assets/129054045/74f55ace-c705-4850-b345-d6578076bb46)

![image](https://github.com/namgyeonghyeon/Flight_Schedule/assets/129054045/c29ea45a-f238-417f-882a-d6484f5da8c5)

새로고침 시간 설정





[오류 해결방안]

1. http traffic 오류   
-> AndroidManifest.xml파일에 <application> 부분에	android:usesCleartextTraffic="true" 로 	
설정해 http를 접속 가능하게 만들었다

2. 파싱할때 double로 만든부분만 출력안되는 오류
-> AirportRecycleViewAdapter 부분의 ViewHolder 부분에서 
binding.setMagnitideformat(MAGNITUDE_FORMAT); 을 쓰지않아 double 부분만 출력이 안됬음

3. 설정에서 풍속부분을 가져올때 일정부분이상의 데이터를 가져오지않던 오류
-> arrays.xml 부분에 wind=values부분에 <item>3</item> <item>6</item> 
Item부분이 3 단위로 나뉘어져 있어서 풍속이 6이상인 데이터부터는 가져오지 못했음
