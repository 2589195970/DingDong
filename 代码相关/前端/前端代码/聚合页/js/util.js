// let baseUrl = 'https://api.haoka.asia/api';
let baseUrl = 'https://api.dingdonghaoka.com/api';
function getQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2])
  }
  return null
}
function checkPhone(phone) {
  var reg = /^1\d{10}$/;
  return phone.length === 11 && reg.test(phone);
}
function checkIdCard(str) {
  var reg=/(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/;
  return reg.test(str);
}
function generateRandom(len) {
  let randomNum = parseInt(Math.random() * Math.pow(10, len)) + '';
  for (let i = 0; i < len - randomNum.length; i++) {
    randomNum = '0' + randomNum;
  }
  return randomNum;
}
function generateUUID() {
  return new Date().getTime() + generateRandom(4);
}

/**
 * Cookie
 */
function setCookie(cname, cvalue, exdays) {
  var d = new Date();
  d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
  var expires = "expires=" + d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function setCookieByExpireDate(cname, cvalue, d) {
  var expires = "expires=" + d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}
function clearAllCookie() {
  var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
  if (keys) {
    for (var i = keys.length; i--; )
      document.cookie = keys[i] + "=0;expires=" + new Date(0).toUTCString();
  }
}

// 检查UUID是否存在
function checkUUID() {
  let uuid = getCookie('uuid');
  if (!uuid) {
    uuid = generateUUID();
    const nextDay = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
    nextDay.setHours(0);
    nextDay.setMinutes(0);
    nextDay.setSeconds(0);
    setCookieByExpireDate('uuid', uuid, nextDay);
  }
  return uuid;
}

/**
 * 靓号判断
 */
 function splitGoodFragment(goodFragment, phone) {
  if (!goodFragment || !phone) {
    // 没输入靓号部分
    return {
      html: phone,
      goodNumber: false,
    };
  }

  const splitRes = phone.split(goodFragment);
  const res = [];
  for(let i=0;i<goodFragment.length;i++){
    phone = phone.replace(goodFragment[i],`<span style="color:red"}>${goodFragment[i]}</span>`);
  }
  splitRes.forEach((frag, idx) => {
    if (frag.length !== 0) {
      res.push({
        fragment: frag,
        hit: false,
      });
    }
    if (idx !== splitRes.length - 1) {
      res.push({
        fragment: goodFragment,
        hit: true,
      });
    }
  });
  return {
    html: phone,
    goodNumber: true,
  };
}
function judgeGoodNumber(phone) {
  let matchRes;
  var execPhoneList = [];
  let regs = [
    // 连续号码
    /((?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)|9(?=0)){2,}\d)/,
    // 年份
    /(19|20)[\d]{1}(1[0-2]|0?[1-9])/,
    // 连号
    /([\d])\1{1,}/,
    // AABB
    /(\d)\1(\d)\2/,
    // ABAB
    /(\d)(\d)\1\2/,
    // ABBA
    /(\w)([^\1])\2\1/,
    // ABB
    /(\d)(\d)\2/,
    /1314/,
    /521/,
    /520/,
    /668/,
    /68/,
    /66/,
    /58/,
    /6/,
    /8/,
  ];
  for (let reg of regs) {
    matchRes = reg.exec(phone);
    if (matchRes) {   
      execPhoneList.push(matchRes[0]); 
      // 靓号号码标价
      if(matchRes[0].length>=4){
        numberPrice =  ((matchRes[0].length+1)*100) + 88 + '元';
      }else{
        numberPrice =  (matchRes[0].length*100) + 88 + '元';
      }
    }
  }
  if(execPhoneList.length!==0){
    return splitGoodFragment(execPhoneList, phone);
  }else{
    numberPrice = '88元';
    return splitGoodFragment(null, phone);
  }
}

/**
 * 获取url参数对象
 */
function getUrlParamObj() {
  const obj = {};
  let kv = [];
  const kvStringArr = window.location.search.substring(1).split('&');
  for (let i in kvStringArr) {
    kv = kvStringArr[i].split('=');
    obj[kv[0]] = kv[1];
  }
  return obj;
}
