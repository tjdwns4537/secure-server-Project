package smilegate.securitySystem.repository;

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
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(sequence, member);
        return member;
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
    public void clear() {
        store.clear();
    }
}
