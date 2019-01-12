
package WSClient;

import Utiles.Retorno_MsgObj;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para impactar_inconsistencia complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="impactar_inconsistencia">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cambios" type="{http://WebService/}retornoMsgObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "impactar_inconsistencia", propOrder = {
    "cambios"
})
public class ImpactarInconsistencia {

    protected Retorno_MsgObj cambios;


    /**
     * Obtiene el valor de la propiedad cambios.
     * 
     * @return
     *     possible object is
     *     {@link Retorno_MsgObj }
     *     
     */
    public Retorno_MsgObj getCambios() {
        return cambios;
    }

    /**
     * Define el valor de la propiedad cambios.
     * 
     * @param value
     *     allowed object is
     *     {@link Retorno_MsgObj }
     *     
     */
    public void setCambios(Retorno_MsgObj value) {
        this.cambios = value;
    }

}
