/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * SDT SDT_NotificacionApp
 *
 * @author alvar
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SDT_NotificacionApp implements Serializable{
    private Long multicast_id;
    private Integer success;
    private Integer failure;
    private Integer canonical_ids;
    private List<SDT_NotificacionAppResultado> results;
    
    
    
    
 ///   private 

          //  {"multicast_id":5110542168779441622,"success":1,"failure":0,"canonical_ids":0,"results":[{"message_id":"0:1502069772556830%6593fe6f6593fe6f"}]}

    public SDT_NotificacionApp() {
    }

    /**
     *
     * @return Retorna multicast_id
     */
    public Long getMulticast_id() {
        return multicast_id;
    }

    /**
     *
     * @param multicast_id Recibe multicast_id
     */
    public void setMulticast_id(Long multicast_id) {
        this.multicast_id = multicast_id;
    }

    /**
     *
     * @return Retorna Correcto
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     *
     * @param success Recibe Correcto
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     *
     * @return Retorna Fallo
     */
    public Integer getFailure() {
        return failure;
    }

    /**
     *
     * @param failure Recibe Fallo
     */
    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    /**
     *
     * @return Retorna canonical_ids
     */
    public Integer getCanonical_ids() {
        return canonical_ids;
    }

    /**
     *
     * @param canonical_ids Recibe canonical_ids
     */
    public void setCanonical_ids(Integer canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    /**
     *
     * @return Retorna Resultados
     */
    public List<SDT_NotificacionAppResultado> getResults() {
        return results;
    }

    /**
     *
     * @param results Recibe Resultados
     */
    public void setResults(List<SDT_NotificacionAppResultado> results) {
        this.results = results;
    }
    
    
}
