class KansasCityShuffler {

    constructor(root = document) {
        this._root = root;
        this._swappedPairs = new Map();
    }   

    _isChildOf(e1, e2) {
        let tmp = e2.parentNode;
        while(tmp) {
            if(e1 === tmp) {
                return true;
            }
            tmp = tmp.parentNode;
        }
        return false;
    }

    _alreadySwapped(e1, e2) {
        return this._swappedPairs.get(e1) === e2;
    }

    _isShuffable(e1, e2) {
        return e1 !== e2 && !this._alreadySwapped(e1,e2) && !this._isChildOf(e1,e2) && !this._isChildOf(e2,e1); // 1) not the same object,
                                                                                                                // 2) not already swapped with the same object (useful for only two objects)
                                                                                                                // 3) e1 is not child of e2
                                                                                                                // 4) e2 is not child of e1
    }

    _collectPossibleSwapsFor(element) {
        const tag = element.tagName;
        const elements = [...this._root.getElementsByTagName(tag)]; // get all elements and convert array-like object to real array.
        return elements
            .filter( e => this._isShuffable(element,e))
            .filter( e => !this._isControl(e) );
    }

    _pickRandomElementFrom(elements) {
        const index = Math.random() * elements.length;
        return elements[Math.floor(index)];
    }

    _indexOf(e1) {
        let i = 0;
        let tmp = e1.parentNode.firstChild;
        while(tmp && tmp != e1) {
            tmp = tmp.nextSibling;
            ++i;
        }
        return i;
    }

    _insertAt(target, element, index) {
        let refNode = target.firstChild;
        let i = 0;
        while(refNode && i !== index) {
            ++i;
            refNode = refNode.nextSibling;
        }
        target.insertBefore(element, refNode);
    }

    _swap(e1, e2) {
        const parent1 = e1.parentNode;
        const parent2 = e2.parentNode;

        const index1 = this._indexOf(e1);
        const index2 = this._indexOf(e2);

        e1.remove();
        e2.remove();

        this._insertAt(parent1, e2, index1);
        this._insertAt(parent2, e1, index2);

        this._swappedPairs.set(e1,e2);
        this._swappedPairs.set(e2,e1);
    }

    _isControl(e) {
        let tmp = e;
        while(tmp) {
            if(tmp.dataset && tmp.dataset.controlTable === "true") {
                return true;
            }
            tmp = tmp.parentNode;
        }
        return false;
    }

    _swapElementsOfTag(tag, onFinished = () => {}) {
        const elements = [...this._root.getElementsByTagName(tag)]
            .filter(e => !this._isControl(e)); //copy to array.
        
        let i = 0;
        const next = () => {
            const e = elements[i];
            const selection = this._collectPossibleSwapsFor(e);
            if(selection.length) {
                const toSwapWith = this._pickRandomElementFrom(selection);
                this._swap(e, toSwapWith);
            }
            ++i;
            if(i < elements.length) {
                setTimeout( next, 500);
            } else {
                onFinished();
            }
        };

        next();
    }

    shuffle(onFinished = () => {}, ...tags) {
        this._swappedPairs.clear();
        if(!tags.length) {
            onFinished();
        }

        let i = 0;
        const next = () => {
            if(i >= tags.length) {
                onFinished();
                return;
            }
            const tag = tags[i];
            ++i;
            this._swapElementsOfTag(tag, next);
        };

        next();
    }


}

const ELEMENTS = ["li", "h1", "h2", "img", "p", "a", "tr"];



window.addEventListener("load", e => {
    const shuffler = new KansasCityShuffler();
    let isShuffling = false;
    function toggleInput(table, enable) {
    	isShuffling = !enable;
        table.querySelectorAll("button").forEach( bttn => {
            bttn.disabled = enable ? "" : "true";
        });
    }

    const controlContainer = document.createElement("div");
    const controlTr = document.createElement("tr");
    const countTr = document.createElement("tr");
    const checkTr = document.createElement("tr");
    const shuffleTr = document.createElement("tr");
    const table = document.createElement("table");
    
    const shuffleTd = document.createElement("td");
    shuffleTd.colSpan = ELEMENTS.length;
    const shuffleAllBttn = document.createElement("button");
    shuffleAllBttn.textContent = "Shuffle!";
    shuffleAllBttn.addEventListener("click", evt => {
        
        const selectedElements = [...document.querySelectorAll("input[type='checkbox']:checked")];
        const tags = selectedElements.map(e => e.dataset.checkboxForElement);
        if(!tags.length) {
            return;
        }
        toggleInput(table, false);
        shuffleAllBttn.disabled = "true";
    	shuffler.shuffle( () => {
            shuffleAllBttn.disabled = "";
            toggleInput(table, true);
        }, ...tags);
    });
    shuffleTd.appendChild(shuffleAllBttn);
    shuffleTr.appendChild(shuffleTd);

    table.appendChild(controlTr);
    table.appendChild(countTr);
    table.appendChild(checkTr);
    table.appendChild(shuffleTr);

    ELEMENTS.forEach( e => {
        const go = document.createElement("button");
        go.addEventListener("click", evt => {
            toggleInput(table, false);
            shuffler.shuffle(() => {
                toggleInput(table, true);
            },e);
        });
        go.textContent = e;
        go.dataset.buttonForElement = e;
        //document.body.insertBefore(go, document.body.firstChild);
        controlContainer.appendChild(go);

        const cntTd = document.createElement("td");
        const cntrlTd = document.createElement("td");
        const checkTd = document.createElement("td");

        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.dataset.checkboxForElement = e;
        
        cntTd.textContent = document.getElementsByTagName(e).length;
        cntrlTd.appendChild(go);
        checkTd.appendChild(checkbox);

        controlTr.appendChild(cntrlTd);
        countTr.appendChild(cntTd);
        checkTr.appendChild(checkTd);
    });
    
    
    table.dataset.controlTable = "true";

    document.body.insertBefore(table, document.body.firstChild);
    
    
    
    window.addEventListener("deviceorientation", eventData =>{
        const MAX_VALUE = 180;
    	const color1 = {red: 42, green: 140, blue: 252};
    	const color2 = {red: 252, green: 42, blue: 42};
        let alpha = Math.abs(eventData.alpha % 360);
        //let alpha = Math.abs(eventData.gamma);
        console.log(alpha);
    	if(alpha > MAX_VALUE){
    		const diff  = alpha - MAX_VALUE;
    		alpha = MAX_VALUE - diff;
        }
    	const colorPart = (alpha/MAX_VALUE)*100;
    	
    	function colorShuffle(c1,c2,percent){
    		return Math.floor( c1 + (c2 - c1) * (percent / 100) );
        }
        const red = colorShuffle(color1.red,color2.red,colorPart);
        const green = colorShuffle(color1.green, color2.green,colorPart);
        const blue = colorShuffle(color1.blue, color2.blue, colorPart);
        const newColor = `rgb(${red},${green},${blue})`;
    	document.body.style.backgroundColor = newColor;
    });

    
    window.addEventListener("devicemotion", eventData =>{
    	if(isShuffling){
    		return;	
    	}
    	if(eventData.acceleration.x > 5|| eventData.acceleration.y > 5|| eventData.acceleration.z > 5){
    		toggleInput(table, false);
    		shuffler.shuffle(() =>{
    			toggleInput(table, true);
    		},...ELEMENTS);
    	}
    });
    
    
});