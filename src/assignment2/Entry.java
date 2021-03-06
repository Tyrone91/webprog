//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.12.18 um 11:54:58 AM CET 
//


package assignment2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für entry complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="entry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{}title"/>
 *         &lt;element name="description" type="{}description"/>
 *         &lt;element name="link" type="{}link"/>
 *         &lt;element name="sourcename" type="{}sourcename"/>
 *         &lt;element name="sourcelink" type="{}sourcelink"/>
 *         &lt;element name="category" type="{}category"/>
 *         &lt;element name="tags" type="{}tags"/>
 *         &lt;element name="pulldate" type="{}pulldate"/>
 *         &lt;element name="pulltime" type="{}pulltime"/>
 *         &lt;element name="pubdate" type="{}pubdate"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entry", propOrder = {
    "title",
    "description",
    "link",
    "sourcename",
    "sourcelink",
    "category",
    "tags",
    "pulldate",
    "pulltime",
    "pubdate"
})
public class Entry {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String link;
    @XmlElement(required = true)
    protected String sourcename;
    @XmlElement(required = true)
    protected String sourcelink;
    @XmlElement(required = true)
    protected String category;
    @XmlElement(required = true)
    protected Tags tags;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar pulldate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar pulltime;
    @XmlElement(required = true)
    protected String pubdate;

    /**
     * Ruft den Wert der title-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Legt den Wert der title-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Ruft den Wert der link-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLink() {
        return link;
    }

    /**
     * Legt den Wert der link-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLink(String value) {
        this.link = value;
    }

    /**
     * Ruft den Wert der sourcename-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcename() {
        return sourcename;
    }

    /**
     * Legt den Wert der sourcename-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcename(String value) {
        this.sourcename = value;
    }

    /**
     * Ruft den Wert der sourcelink-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcelink() {
        return sourcelink;
    }

    /**
     * Legt den Wert der sourcelink-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcelink(String value) {
        this.sourcelink = value;
    }

    /**
     * Ruft den Wert der category-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Legt den Wert der category-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Ruft den Wert der tags-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Tags }
     *     
     */
    public Tags getTags() {
        return tags;
    }

    /**
     * Legt den Wert der tags-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Tags }
     *     
     */
    public void setTags(Tags value) {
        this.tags = value;
    }

    /**
     * Ruft den Wert der pulldate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPulldate() {
        return pulldate;
    }

    /**
     * Legt den Wert der pulldate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPulldate(XMLGregorianCalendar value) {
        this.pulldate = value;
    }

    /**
     * Ruft den Wert der pulltime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPulltime() {
        return pulltime;
    }

    /**
     * Legt den Wert der pulltime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPulltime(XMLGregorianCalendar value) {
        this.pulltime = value;
    }

    /**
     * Ruft den Wert der pubdate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPubdate() {
        return pubdate;
    }

    /**
     * Legt den Wert der pubdate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPubdate(String value) {
        this.pubdate = value;
    }

}
