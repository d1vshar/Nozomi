package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.ApplicantsRecord;
import org.jooq.DSLContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Applicants.APPLICANTS;

public class GuildApplicants {
  private final DSLContext db;
  private final Long id;

  GuildApplicants(DSLContext db, long id) {
    this.db = db;
    this.id = id;
  }

  public List<Integer> getAllApplicants() {
    List<Integer> result = new ArrayList<>();
    result.add(0);
    ApplicantsRecord fetch = db.selectFrom(APPLICANTS).where(APPLICANTS.ID.eq(id)).fetchOne();
    if (fetch != null && fetch.getApplicants() != null)
      result.addAll(Arrays.asList(fetch.getApplicants()));
    return result;
  }

  public void update(List<Integer> list) {
    db.delete(APPLICANTS).execute();
    db.insertInto(APPLICANTS)
        .columns(APPLICANTS.ID, APPLICANTS.APPLICANTS_)
        .values(id, list.toArray(new Integer[0]))
        .execute();
  }
}
