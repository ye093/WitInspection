
      !(function(){
        var uniAppViewReadyCallback = function(){
          setCssToHead([".",[1],"grace-slider{position: relative; width: 100%; height:40px;}\n.",[1],"grace-sliders{position:absolute; width:88%; padding:0; left:0}\n",])();
document.dispatchEvent(new CustomEvent("generateFuncReady", { detail: { generateFunc: $gwx('./pages/slider/slider.wxml') } }));
        }
        if(window.__uniAppViewReady__){
          uniAppViewReadyCallback()
        }else{
          document.addEventListener('uniAppViewReady',uniAppViewReadyCallback)
        }
      })();
      