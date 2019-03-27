
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead(["wx-swiper-item{background:#F3F4F5; line-height:",[0,350],"; text-align:center; font-size:36px; color:#888;}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/tab/tab.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      