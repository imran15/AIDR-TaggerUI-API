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
import qa.qcri.aidr.predictui.dto.CrisisAttributesDTO;
import qa.qcri.aidr.predictui.dto.TrainingDataDTO;
import qa.qcri.aidr.predictui.facade.MiscResourceFacade;
import java.util.Date;

/**
 *
 * @author Imran
 */
@Stateless
public class MiscResourceImp implements MiscResourceFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    @Override
    public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(int crisisID, int modelID, int fromRecord, int limit) {
        List<TrainingDataDTO> trainingDataList = new ArrayList();
        String sql = " SELECT lbl.nominalLabelID, lbl.name labelName, d.data, u.userID, u.name, dnl.timestamp \n"
                + " FROM document_nominal_label dnl\n"
                + " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID\n"
                + " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID \n"
                + " JOIN model m on m.modelFamilyID= mf.modelFamilyID \n"
                + " JOIN document d on d.documentID = dnl.documentID \n"
                + " JOIN task_answer ta on ta.documentID = d.documentID \n"
                + " JOIN users u on u.userID = ta.userID \n"
                + " AND d.crisisID = :crisisID \n"
                + " WHERE mf.modelID = :modelID LIMIT :fromRecord, :limit";
        
        String sqlCount = " SELECT count(*)  \n"
                + " FROM document_nominal_label dnl\n"
                + " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID\n"
                + " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID \n"
                + " JOIN model m on m.modelFamilyID= mf.modelFamilyID \n"
                + " JOIN document d on d.documentID = dnl.documentID \n"
                + " JOIN task_answer ta on ta.documentID = d.documentID \n"
                + " JOIN users u on u.userID = ta.userID \n"
                + " AND d.crisisID = :crisisID \n"
                + " WHERE m.modelID = :modelID ";
        try {
            Integer totalRows = 0;
            
            if(fromRecord == 0){
            Query queryCount = em.createNativeQuery(sqlCount);
            queryCount.setParameter("crisisID", crisisID);
            queryCount.setParameter("modelID", modelID);
            Object res = queryCount.getSingleResult();
            totalRows = Integer.parseInt(res.toString());
            }
            
            Query query = em.createNativeQuery(sql);
            query.setParameter("crisisID", crisisID);
            query.setParameter("modelID", modelID);
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
}
