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
        return elements.filter( e => this._isShuffable(element,e));
    }

    _pickRandomElementFrom(elements) {
        const index = Math.random() * elements.length;
        return elements[Math.floor(index)];
    }

    _indexOf(e1) {
        let i = 0;
        let tmp = e1.parentNode.firstElementChild;
        while(tmp && tmp != e1) {
            tmp = tmp.nextElementSibling;
            ++i;
        }
        return i;
    }

    _insertAt(target, element, index) {
        let refNode = target.firstElementChild;
        let i = 0;
        while(refNode && i !== index) {
            ++index;
            refNode = refNode.nextElementSibling;
        }
        target.insertBefore(element, refNode);
    }

    _swap(e1, e2) {
        const parent1 = e1.parentNode;
        const parent2 = e2.parentNode;

        const index1 = this._indexOf(e1);
        const index2 = this._indexOf(e2);

        this._insertAt(parent1, e2, index1);
        this._insertAt(parent2, e1, index2);

        this._swappedPairs.set(e1,e2);
        this._swappedPairs.set(e2,e1);
    }

    _swapElementsOfTag(tag) {
        const elements = [...this._root.getElementsByTagName(tag)]; //copy to array.
        elements.forEach( e => {
            const selection = this._collectPossibleSwapsFor(e);
            if(selection.length) {
                const toSwapWith = this._pickRandomElementFrom(selection);
                this._swap(e, toSwapWith);
            }
        });
    }

    shuffle(...tags) {
        this._swappedPairs.clear();
        tags.forEach( tag => this._swapElementsOfTag(tag));
    }


}

window.addEventListener("load", e => {
    const shuffler = new KansasCityShuffler();

    const go = document.createElement("button");
    go.addEventListener("click", e => {
        shuffler.shuffle("li");
    });
    go.textContent = "Shuffle";

    document.body.insertBefore(go, document.body.firstChild);
})