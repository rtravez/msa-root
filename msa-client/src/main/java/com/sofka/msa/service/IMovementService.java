package com.sofka.msa.service;

import com.sofka.msa.dto.request.MovementRequest;
import com.sofka.msa.dto.response.MovementReportResponse;
import com.sofka.msa.dto.response.MovementResponse;
import com.sofka.msa.entity.MovementEntity;
import com.sofka.msa.exception.ExceptionManager;

import java.util.List;

/**
 * <b> Description de la class, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
public interface IMovementService extends IGenericService<MovementEntity, Long> {

    /**
     * Process save movement
     *
     * @param request
     * @return
     * @throws ExceptionManager
     */
    MovementResponse processSaveMovement(MovementRequest request) throws ExceptionManager;

    /**
     * Find report movement
     *
     * @param initialDate
     * @param finalDate
     * @param identification
     * @param accountType
     * @return
     * @throws ExceptionManager
     */
    List<MovementReportResponse> findMovementByDateAndIdentification(String initialDate, String finalDate, String identification, String accountType) throws ExceptionManager;

    /**
     * Find movement by account id
     *
     * @param accountId
     * @return true or false
     * @throws ExceptionManager
     */
    boolean findMovementByAccountId(Long accountId) throws ExceptionManager;

    /**
     * Delete movement
     *
     * @param id
     * @return
     * @throws ExceptionManager
     */
    int deleteMovementById(Long id) throws ExceptionManager;
}
