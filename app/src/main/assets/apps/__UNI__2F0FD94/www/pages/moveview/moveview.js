
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead(["wx-movable-area{background:#F0F0F0; width:100%; height:",[0,300],";}\nwx-movable-view{width:",[0,120],"; height:",[0,120],"; background:#FFFFFF;}\nwx-movable-view wx-image{width:",[0,120],"; height:",[0,120],"; border-radius:",[0,5],";}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/moveview/moveview.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      