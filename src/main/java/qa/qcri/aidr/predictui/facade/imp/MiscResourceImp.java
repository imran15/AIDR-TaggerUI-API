/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.dto.CrisisAttributesDTO;
import qa.qcri.aidr.predictui.dto.TrainingDataDTO;
import qa.qcri.aidr.predictui.facade.MiscResourceFacade;
import java.util.Date;
import qa.qcri.aidr.predictui.dto.ItemToLabelDTO;
import qa.qcri.aidr.predictui.dto.NominalAttributeDTO;
import qa.qcri.aidr.predictui.dto.NominalLabelDTO;

/**
 *
 * @author Imran
 */
@Stateless
public class MiscResourceImp implements MiscResourceFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    @Override
    public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(int crisisID, int modelFamilyID, int fromRecord, int limit) {
        List<TrainingDataDTO> trainingDataList = new ArrayList();
        String sql = " SELECT lbl.nominalLabelID, lbl.name labelName, d.data, u.userID, u.name, dnl.timestamp \n"
                + " FROM document_nominal_label dnl\n"
                + " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID\n"
                + " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID \n"
                //+ " JOIN model m on m.modelFamilyID= mf.modelFamilyID \n"
                + " JOIN document d on d.documentID = dnl.documentID \n"
                + " JOIN task_answer ta on ta.documentID = d.documentID \n"
                + " JOIN users u on u.userID = ta.userID \n"
                + " AND d.crisisID = :crisisID \n"
                + " WHERE mf.modelFamilyID = :modelFamilyID LIMIT :fromRecord, :limit";
        
        String sqlCount = " SELECT count(*)  \n"
                + " FROM document_nominal_label dnl\n"
                + " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID\n"
                + " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID \n"
                //+ " JOIN model m on m.modelFamilyID= mf.modelFamilyID \n"
                + " JOIN document d on d.documentID = dnl.documentID \n"
                + " JOIN task_answer ta on ta.documentID = d.documentID \n"
                + " JOIN users u on u.userID = ta.userID \n"
                + " AND d.crisisID = :crisisID \n"
                + " WHERE m.modelFamilyID = :modelFamilyID ";
        try {
            Integer totalRows = 0;
            
            if(fromRecord == 0){
            Query queryCount = em.createNativeQuery(sqlCount);
            queryCount.setParameter("crisisID", crisisID);
            queryCount.setParameter("modelFamilyID", modelFamilyID);
            Object res = queryCount.getSingleResult();
            totalRows = Integer.parseInt(res.toString());
            }
            
            Query query = em.createNativeQuery(sql);
            query.setParameter("crisisID", crisisID);
            query.setParameter("modelFamilyID", modelFamilyID);
            query.setParameter("fromRecord", fromRecord);
            query.setParameter("limit", limit);
            
            List<Object[]> rows = query.getResultList();
            if (rows.isEmpty()){
                return trainingDataList;
            }
            TrainingDataDTO trainingDataRow;
            for (Object[] row : rows) {
                trainingDataRow = new TrainingDataDTO();
                trainingDataRow.setLabelID(((Integer) row[0]).intValue());
                trainingDataRow.setLabelName((String) row[1]);
                trainingDataRow.setTweetJSON((String) row[2]);
                trainingDataRow.setLabelerID(((Integer) row[3]).intValue());
                trainingDataRow.setLabelerName((String) row[4]);
                trainingDataRow.setLabeledTime(((Date) row[5]));
                trainingDataRow.setTotalRows(totalRows);
                trainingDataList.add(trainingDataRow);
            }
            return trainingDataList;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ItemToLabelDTO getItemToLabel(int crisisID, int attributeID) {
        // with attributeID get attribute and labels details
        // with crisisID get an item from document table for which hasHumanLabel is FALSE
        // packup both info into one class DTO and return
        
        String sqlToGetAttribute = "SELECT code, name, description FROM nominal_attribute WHERE nominalAttributeID = :attributeID";
        
        String sqlToGetLabel = "SELECT nominalLabelCode, name, description FROM nominal_label WHERE nominalAttributeID = :attributeID";
        //String sqlToGetItem = "SELECT data FROM document WHERE crisisID = :crisisID";
        NominalAttributeDTO attributeDTO = new NominalAttributeDTO();
        ItemToLabelDTO itemToLabel = new ItemToLabelDTO();
        try{
            Query attributeQuery = em.createNativeQuery(sqlToGetAttribute);
            attributeQuery.setParameter("attributeID", attributeID);
            List<Object[]> attributeResults = attributeQuery.getResultList();
            System.out.println(attributeResults.get(0)[0]); 
            attributeDTO.setCode((String) attributeResults.get(0)[0]);
            attributeDTO.setName((String) attributeResults.get(0)[1]);
            attributeDTO.setDescription((String) attributeResults.get(0)[2]);
            
            Query labelQuery = em.createNativeQuery(sqlToGetLabel);
            labelQuery.setParameter("attributeID", attributeID);
            List<Object[]> labelsResults = labelQuery.getResultList();
            
            Collection<NominalLabelDTO> labelDTOList = new ArrayList<NominalLabelDTO>();
            
            for (Object[] label: labelsResults){
                NominalLabelDTO labelDTO = new NominalLabelDTO();
                labelDTO.setNominalLabelCode((String)label[0]);
                labelDTO.setName((String) label[1]);
                labelDTO.setDescription((String) label[2]);
                
                labelDTOList.add(labelDTO);
            }
            attributeDTO.setNominalLabelCollection(labelDTOList);
            
            //here retrieve data from document table
            
            itemToLabel.setAttribute(attributeDTO);
            
        }catch(NoResultException e) {
            return null;
            
        }
        return itemToLabel;
    }
}
