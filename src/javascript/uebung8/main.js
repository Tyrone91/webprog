window.addEventListener("load", event => {
    const elements = ["ol", "ul", "li", "h1", "h2", "p"];

    elements.forEach( element => {
        const countControls = document.getElementById("controls-count");
        const deleteControls = document.getElementById("controls-delete");

        const cntBttn = document.createElement("button");
        cntBttn.addEventListener("click", e => {
            const out = document.getElementById("output");
            const count  = document.getElementsByTagName(element).length;
            out.innerText = `count for '${element}' is ${count}`;
        });
        cntBttn.textContent = element;
        cntBttn.dataset.owner = element;

        const deleteBttn = document.createElement("button");
        deleteBttn.addEventListener("click", e => {
            [...document.getElementsByTagName(element)].forEach( el => el.remove());
            cntBttn.remove();
            deleteBttn.remove();
            //document.querySelectorAll(`[data-owner=${element}]`).forEach( b => b.remove() );
            
        });
        deleteBttn.textContent = element;
        deleteBttn.dataset.owner = element;

        countControls.appendChild(cntBttn);
        deleteControls.appendChild(deleteBttn);
    });
});