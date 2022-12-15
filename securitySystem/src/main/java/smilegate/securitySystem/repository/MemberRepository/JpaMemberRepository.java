package smilegate.securitySystem.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import smilegate.securitySystem.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepositoryInterface{

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery(
                "select m from Member m", Member.class
        ).getResultList();
    }

    @Override
    public void clear() {
        em.clear();
    }

    public List<Member> findByName(String name) {
        return em.createQuery(
                "select m from Member m where m.name = :name", Member.class
        ).setParameter("name", name).getResultList();
    }

    public List<Member> findByEmail(String email) {
        return em.createQuery(
                "select m from Member m where m.email = :email", Member.class
        ).setParameter("email", email).getResultList();
    }
}
