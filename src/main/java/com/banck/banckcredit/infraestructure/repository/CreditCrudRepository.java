package com.banck.banckcredit.infraestructure.repository;

import com.banck.banckcredit.domain.Credit;
import com.banck.banckcredit.infraestructure.model.dao.CreditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.banck.banckcredit.aplication.model.CreditRepository;

/**
 *
 * @author jonavcar
 */
@Component
public class CreditCrudRepository implements CreditRepository {

    @Autowired
    ICreditCrudRepository creditRepository;

    @Override
    public Mono<Credit> get(String id) {
        return creditRepository.findById(id).map(this::CreditDaoToCredit);
    }

    @Override
    public Flux<Credit> list() {
        return creditRepository.findAll().map(this::CreditDaoToCredit);
    }

    @Override
    public Mono<Credit> create(Credit c) {
        return creditRepository.save(CreditToCreditDao(c)).map(this::CreditDaoToCredit);
    }

    @Override
    public Mono<Credit> update(String id, Credit credit) {
        credit.setCredit(id);
        return creditRepository.save(CreditToCreditDao(credit)).map(this::CreditDaoToCredit);
    }

    @Override
    public void delete(String id) {
        creditRepository.deleteById(id).subscribe();
    }

    @Override
    public Flux<Credit> listByCustomer(String customer) {
        return creditRepository.findAllByCustomer(customer).map(this::CreditDaoToCredit);
    }

    public CreditDao CreditToCreditDao(Credit credit) {
        CreditDao creditDao = new CreditDao();
        creditDao.setCredit(credit.getCredit());
        creditDao.setCustomer(credit.getCustomer());
        creditDao.setCustomerType(credit.getCustomerType());
        creditDao.setCreditType(credit.getCreditType());
        creditDao.setDateCreated(credit.getDateCreated());
        creditDao.setLimitAmount(credit.getLimitAmount());
        creditDao.setActive(credit.isActive());
        return creditDao;
    }

    public Credit CreditDaoToCredit(CreditDao ad) {
        Credit credit = new Credit();
        credit.setCredit(ad.getCredit());
        credit.setCustomer(ad.getCustomer());
        credit.setCustomerType(ad.getCustomerType());
        credit.setCreditType(ad.getCreditType());
        credit.setDateCreated(ad.getDateCreated());
        credit.setLimitAmount(ad.getLimitAmount());
        credit.setActive(ad.isActive());
        return credit;
    }

    @Override
    public Flux<Credit> listByCustomerAndCreditType(String customer, String creditType) {
        return creditRepository.findAllByCustomerAndCreditType(customer, creditType).map(this::CreditDaoToCredit);
    }

}
