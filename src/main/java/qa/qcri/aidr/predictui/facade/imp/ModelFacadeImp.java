/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import qa.qcri.aidr.predictui.facade.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.Model;
import qa.qcri.aidr.predictui.entities.ModelFamily;
import qa.qcri.aidr.predictui.util.ModelWrapper;

/**
 *
 * @author Imran
 */
@Stateless
public class ModelFacadeImp implements ModelFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public List<Model> getAllModels() {
        Query query = em.createNamedQuery("Model.findAll", Model.class);
        List<Model> modelsList = query.getResultList();
        return modelsList;

    }

    public Model getModelByID(int id) {
        Model model = null;
        try {
            Query query = em.createNamedQuery("Model.findByModelID", Model.class);
            query.setParameter("modelID", id);
            model = (Model) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return model;
    }

    public List<Model> getModelByModelFamilyID(int modelFamilyID) {
        ModelFamily modelFamily = em.find(ModelFamily.class, modelFamilyID);
        Query query = em.createNamedQuery("Model.findByModelFamilyID", Model.class);
        query.setParameter("modelFamily", modelFamily);
        List<Model> modelLlist = query.getResultList();

        return modelLlist;
    }

    public List<ModelWrapper> getModelByCrisisID(int crisisID) {
        List<ModelWrapper> modelWrapperList = null;
        Crisis crisis = em.find(Crisis.class, crisisID);
        if (crisis != null) {
            modelWrapperList = new ArrayList<ModelWrapper>();
            Collection<ModelFamily> modelFamilyList = crisis.getModelFamilyCollection();
            // for each modelFamily get all the models and take avg
            for (ModelFamily modelFamily : modelFamilyList) {
                Collection<Model> modelList = modelFamily.getModelCollection();
                ModelWrapper modelWrapper = new ModelWrapper();
                long trainingExamples = 0;
                long classigiedElements = 0;
                double auc = 0.0;
                double aucAverage = 0.0;
                int modelID = 0;

//                if size 0 we will get NaN for aucAverage
                if (modelList.size() > 0) {
                    for (Model model : modelList) {
                        trainingExamples += model.getTrainingCount();
                        auc += model.getAvgAuc();
                        modelID = model.getModelID();
                    }
                    aucAverage = auc / modelList.size();
                }
                modelWrapper.setAttribute(modelFamily.getNominalAttribute().getName());
                modelWrapper.setAuc(aucAverage);
                modelWrapper.setClassifiedDocuments(0); //TODO: putting it for now as zero
                modelWrapper.setStatus(""); //TODO: setting status as empty text, 
                modelWrapper.setTrainingExamples(trainingExamples);
                modelWrapper.setModelID(modelID);

                modelWrapperList.add(modelWrapper);
            }
        }
        return modelWrapperList;

    }
}
