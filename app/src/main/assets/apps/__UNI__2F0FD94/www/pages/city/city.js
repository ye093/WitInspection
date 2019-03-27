
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead([".",[1],"header-line{height:55px;}\n.",[1],"grace-search{position:fixed; left:0; top:0; background:#F1F2F3; padding:",[0,15]," 0; z-index:99;}\n.",[1],"grace-search-in{width:",[0,500],"; margin:0 auto; background:#FFF;}\n.",[1],"grace-city-search{background:#FFF; padding:",[0,30]," 0;}\n.",[1],"grace-city-AZ{background:#FFF; text-indent:",[0,20],"; line-height:",[0,88],";}\n.",[1],"grace-city-AZ-right{background:#FFFFFF; width:",[0,50],"; position:fixed; right:0; top:60px;}\n.",[1],"grace-city-AZ-right wx-view{line-height:",[0,42],"; font-size:",[0,28],"; text-align:center;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/city/city.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      