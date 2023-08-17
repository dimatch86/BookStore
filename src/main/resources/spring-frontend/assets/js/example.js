document.querySelectorAll('button.Spoiler-trigger').forEach(bttn=>{
    bttn.dataset.state=0;
    bttn.addEventListener('click',function(e){
        let span=this.previousElementSibling;
        span.dataset.tmp=span.textContent;
        span.textContent=span.dataset.content;
        span.dataset.content=span.dataset.tmp;

        this.dataset.state=1-this.dataset.state;
    })
});

document.querySelectorAll('span.p1').forEach(span=>{
    span.dataset.content=span.textContent;
    span.textContent=span.textContent.substr(0,200) + '...';
})