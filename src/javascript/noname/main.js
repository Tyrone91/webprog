/**
 * 
 * @param {HTMLElement[]} o 
 */
function augment(o) {
    /**
     * 
     * @param {string} event 
     * @param {(arg0: Event) => void} callback 
     */
    o.on = (event, callback) => {
        o.forEach( e => e.addEventListener(event, callback) );
        return o;
    };

    o.addClass = clazz => {
        o.forEach(e => e.classList.add(clazz))
        return o
    };

    o.removeClass = clazz => {
        o.forEach(e => e.classList.remove(clazz) );
        return o;
    };

    o.insertAfter = (...e) => {
        if(o.length <= 0 ) {
            return o;
        }
        const target = o.first().ele();
        const parent = target.parentNode ? target.parentNode : document;
        e.forEach( ee => parent.insertBefore(ee, target.nextSibling));
        return o;
    };

    o.append = (...es) => {
        es.forEach( e => o.forEach(i => i.appendChild(e)) )
        return o;
    };

    o.prepend = (...es) => {
        es.forEach( e => o.forEach(i => i.insertBefore(e,i)) );
        return o;
    };

    o.attr = (key,val) => {
        o.forEach( e => e.setAttribute(key,val));
        return o;
    };

    o.query = query => {
        const res = o.map( e => [...e.querySelectorAll(query)])
            .reduce( ( prev, cur) =>  prev.concat(cur), []);
        return augment(res);
    };

    o.first = () => augment([o[0]]);

    o.last = () => augemnt([o[o.length-1]]);

    o.get = i => {
        if(i >= o.length) {
            throw "OutOfBounds: " + i;
        }
        return augment([o[i]]);
    };

    o.ele = (i = 0) => {
        return o[i];
    }

    o.is = str => {
        if(o.length > 1 || o.length == 0) {
            return false;
        }
        return o.ele().nodeName.toLowerCase() == str;
    }

    return o;
}

function $(query) {
    if(typeof query == "string") {
        const r = document.querySelectorAll(query);
        return augment([...r]);
    }
    return augment( Array.isArray(query) ? [...query] : [query]);
}

$.create = function(ele) {
    return document.createElement(ele);
}

let selected = null;

window.addEventListener("load", function() {

    
    $(".draggable-list.drag-entries > li")
        .attr("draggable", true)
        .on("dragstart", e => {
            if(!$(e.target).is("li")) {
                return;
            }
            selected = e.target;
            e.dataTransfer.setData('text/plain',"");
        })
        .on("dragover", e => {
            e.preventDefault();
        })
        .on("dragenter", e => {
            $(e.target).addClass("drop-target");
        })
        .on("dragleave",e => {
            $(e.target).removeClass("drop-target");
        })
        .on("drop", e => {
            if(!$(e.target).is("li")) {
                return;
            }
            if(!selected) {
                return;
            }
            $(e.target).insertAfter(selected);
            selected = null;
            $(e.target).removeClass("drop-target");
        });
});

$(window).on("load", e => {
    const TMP_CLASS = "tmp_id_drag_target_" + Date.now();
    $(".draggable-list")
        .attr("draggable", true)
        .on("dragover", e => e.preventDefault())
        .on("dragstart", e => {
            if($(e.target).is("li")) {
                return;
            }
            $(e.currentTarget).addClass(TMP_CLASS);
            e.dataTransfer.setData('text/plain', "." + TMP_CLASS);
        })
        .on("dragenter", e => {
            $(e.target).addClass("drop-target");
        })
        .on("dragleave",e => {
            $(e.target).removeClass("drop-target");
        })
        .on("drop", e => {
            if($(e.target).is("li")) {
                return;
            }

            $(e.target).removeClass("drop-target");                                         
            if(selected) {
                $(e.target).append(selected);
                selected = null;
                return;
            }
            const event = /** @type {DragEvent}*/ (e);
            const selector  = event.dataTransfer.getData("text/plain");
            if(!selector) return;
            const children  = $(selector).query("li");
            
            children.forEach( c => $(e.target).append(c));
        });
});

