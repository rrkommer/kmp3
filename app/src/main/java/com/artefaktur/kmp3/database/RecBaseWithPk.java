package com.artefaktur.kmp3.database;

import org.apache.commons.lang3.StringUtils;

public abstract class RecBaseWithPk extends RecBase {

  public RecBaseWithPk(Mp3Db db, String[] rec) {
    super(db, rec);
  }

  abstract String getPk();

  @Override
  public boolean equals(Object other) {
    if ((other instanceof RecBaseWithPk) == false) {
      return false;
    }
    RecBaseWithPk otrack = (RecBaseWithPk) other;
    return StringUtils.equals(getPk(), otrack.getPk());
  }

  public int hashCode() {
    return getPk().hashCode();
  }
}
