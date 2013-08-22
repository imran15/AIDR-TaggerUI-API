/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.NominalLabel;
import qa.qcri.aidr.predictui.facade.NominalLabelResourceFacade;

/**
 *
 * @author Imran
 */
@Stateless
public class NominalLabelResourceImp implements NominalLabelResourceFacade  {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;
    

    public NominalLabel addNominalLabel(NominalLabel label) {
        em.persist(label);
        return label;
    }

    public NominalLabel getNominalLabelByID(int id) {
        NominalLabel label = null;
        
        Query query = em.createNamedQuery("Crisis.findByCrisisID", Crisis.class);
        query.setParameter("crisisID", id);
        if (query.getSingleResult() != null)
        label = (NominalLabel)query.getSingleResult();
        
        return label;
    }

    public NominalLabel editNominalLabel(NominalLabel crisis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<NominalLabel> getAllNominalLabel() {
        List<NominalLabel> labelList = new ArrayList<NominalLabel>();
        Query q = em.createNamedQuery("NominalLabel.findAll", NominalLabel.class);
        labelList = q.getResultList();
        return labelList;
    }

    
    
    
}
