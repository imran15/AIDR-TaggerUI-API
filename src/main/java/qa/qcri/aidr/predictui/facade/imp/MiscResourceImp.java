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
    public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(int crisisID, int attributeID, int fromRecord, int limit) {
        List<TrainingDataDTO> trainingDataList = new ArrayList();
        String sql = " SELECT lbl.nominalLabelID, lbl.name labelName, d.data, u.userID, u.name, dnl.timestamp \n"
                + " FROM document_nominal_label dnl\n"
                + " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID\n"
                + " JOIN document d on d.documentID = dnl.documentID \n"
                + " JOIN task_answer ta on ta.documentID = d.documentID \n"
                + " JOIN users u on u.userID = ta.userID \n"
                + " AND d.crisisID = :crisisID \n"
                + " WHERE lbl.nominalAttributeID = :attributeID LIMIT :fromRecord, :limit";
        try {
            Query query = em.createNativeQuery(sql);
            query.setParameter("crisisID", crisisID);
            query.setParameter("attributeID", attributeID);
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
                trainingDataList.add(trainingDataRow);
            }
            return trainingDataList;
        } catch (NoResultException e) {
            return null;
        }
    }
}
