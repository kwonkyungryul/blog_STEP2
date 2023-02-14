const koreanLunch = [
    "비빔밥",
    "김치찌개",
    "된장찌개",
    "제육덮밥",
    "국수",
    "삼계탕",
    "불고기",
    "순대국",
    "고추잡채밥",
    "순두부찌개"
  ];
  
  const westernLunch = [
    "스테이크",
    "햄버거",
    "피자",
    "샌드위치",
    "스파게티",
    "샐러드",
    "스프",
    "양장피",
    "덮밥",
    "파스타"
  ];
  
  const chineseLunch = [
    "짜장면",
    "짬뽕",
    "탕수육",
    "울면",
    "마파두부",
    "깐풍기",
    "양꼬치",
    "샤워탕",
    "군만두",
    "유산슬"
  ];
  
  function getLunch() {
    let randomKoreanLunch = koreanLunch[Math.floor(Math.random() * koreanLunch.length)];
    let randomWesternLunch = westernLunch[Math.floor(Math.random() * westernLunch.length)];
    let randomChineseLunch = chineseLunch[Math.floor(Math.random() * chineseLunch.length)];
  
    console.log(`오늘 점심은 한식 - ${randomKoreanLunch}, 양식 - ${randomWesternLunch}, 중식 - ${randomChineseLunch} 어떠세요?`);
  }
  
  getLunch();