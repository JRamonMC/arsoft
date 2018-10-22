//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2017.05.03 a las 05:44:09 PM CEST 
//


package servicio.tipos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="URLprograma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="URLimagen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="emision" type="{http://www.example.org/ejercicio1-2}tipo_emision" maxOccurs="unbounded"/>
 *         &lt;element name="producto" type="{http://www.example.org/ejercicio1-2}tipo_producto" maxOccurs="3" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nombre",
    "urLprograma",
    "urLimagen",
    "emision",
    "producto"
})
@XmlRootElement(name = "programa")
public class Programa {

    @XmlElement(required = true)
    protected String nombre;
    @XmlElement(name = "URLprograma", required = true)
    protected String urLprograma;
    @XmlElement(name = "URLimagen", required = true)
    protected String urLimagen;
    @XmlElement(required = true)
    protected List<TipoEmision> emision;
    protected List<TipoProducto> producto;
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad urLprograma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLprograma() {
        return urLprograma;
    }

    /**
     * Define el valor de la propiedad urLprograma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLprograma(String value) {
        this.urLprograma = value;
    }

    /**
     * Obtiene el valor de la propiedad urLimagen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLimagen() {
        return urLimagen;
    }

    /**
     * Define el valor de la propiedad urLimagen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLimagen(String value) {
        this.urLimagen = value;
    }

    /**
     * Gets the value of the emision property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the emision property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmision().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoEmision }
     * 
     * 
     */
    public List<TipoEmision> getEmision() {
        if (emision == null) {
            emision = new ArrayList<TipoEmision>();
        }
        return this.emision;
    }

    /**
     * Gets the value of the producto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the producto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProducto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoProducto }
     * 
     * 
     */
    public List<TipoProducto> getProducto() {
        if (producto == null) {
            producto = new ArrayList<TipoProducto>();
        }
        return this.producto;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
