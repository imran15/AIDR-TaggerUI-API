/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
public class NominalLabelResourceImp implements NominalLabelResourceFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public NominalLabel addNominalLabel(NominalLabel label) {
        //if (label != null) {
            em.persist(label);
       // }
        return label;
    }

    public NominalLabel getNominalLabelByID(int id) {
        NominalLabel label = null;

        Query query = em.createNamedQuery("NominalLabel.findByNominalLabelID", NominalLabel.class);
        query.setParameter("nominalLabelID", id);
        if (query.getSingleResult() != null) {
            label = (NominalLabel) query.getSingleResult();
        }

        return label;
    }

    @Override
    public NominalLabel editNominalLabel(NominalLabel label) {
        if (label == null) {
            return null;
        }
        try {
            NominalLabel dbLabel = em.find(NominalLabel.class, label.getNominalLabelID());
            if (dbLabel != null) {
                em.merge(label);
                return label;
            }
        } catch (NoResultException e) {
            return null;
        }
        return label;

    }

    public List<NominalLabel> getAllNominalLabel() {
        List<NominalLabel> labelList = new ArrayList<NominalLabel>();
        Query q = em.createNamedQuery("NominalLabel.findAll", NominalLabel.class);
        labelList = q.getResultList();
        return labelList;
    }

    @Override
    public void deleteNominalLabel(int labelID) {
        NominalLabel label = em.find(NominalLabel.class, labelID);
        if (label != null){
            em.remove(label);
        }
    }
}
