package smilegate.securitySystem.repository.MemberRepository;

import smilegate.securitySystem.domain.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemberRepositoryImp implements MemberRepositoryInterface{

    private MemberRepositoryImp() {

    }

    private static final MemberRepositoryImp instance = new MemberRepositoryImp();
    private HashMap<Long, Member> store = new HashMap<>();
    private Long sequence = 0L;

    public static MemberRepositoryInterface getInstance() {
        return instance;
    }

    @Override
    public void save(Member member) {
        member.setId(++sequence);
        store.put(sequence, member);
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Member> findByName(String name) {
        return null;
    }

    @Override
    public List<Member> findByEmail(String email) {
        return null;
    }

    @Override
    public void clear() {
        store.clear();
    }
}
