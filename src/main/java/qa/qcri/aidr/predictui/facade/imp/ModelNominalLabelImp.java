/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.facade.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.entities.Model;
import qa.qcri.aidr.predictui.entities.ModelNominalLabel;

/**
 *
 * @author Imran
 */
@Stateless
public class ModelNominalLabelImp implements ModelNominalLabelFacade{
    
    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public List<ModelNominalLabel> getAllModelNominalLabels() {
        Query query = em.createNamedQuery("ModelNominalLabel.findAll", ModelNominalLabel.class);
        List<ModelNominalLabel> modelNominalLabelList = query.getResultList();
        return modelNominalLabelList;
        
    }

    public List<ModelNominalLabel> getAllModelNominalLabelsByModelID(int modelID) {
        List<ModelNominalLabel> modelNominalLabelList=null;
        Model model = em.find(Model.class, modelID);
        if (model != null){
        Query query = em.createNamedQuery("ModelNominalLabel.findByModel", ModelNominalLabel.class);
        query.setParameter("model", model);
        modelNominalLabelList = query.getResultList();
        }
        return modelNominalLabelList;
    }
    
}
