package com.yongyongwang.multimedia.choose.compress.model;

import com.yongyongwang.multimedia.choose.entity.MultimediaEntity;

public interface OnCompressListener {

  /**
   * Fired when the compression is started, override to handle in your own code
   */
  void onStart();

  /**
   * Fired when a compression returns successfully, override to handle in your own code
   */
  void onSuccess(MultimediaEntity entity);

  /**
   * Fired when a compression fails to complete, override to handle in your own code
   */
  void onError(Throwable e);
}
