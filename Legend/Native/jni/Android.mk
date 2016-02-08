

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= legend_native.cpp

LOCAL_CFLAGS	:= -std=gnu++11 -fpermissive

LOCAL_SHARED_LIBRARIES := 
    
LOCAL_LDLIBS    := -llog

LOCAL_STATIC_LIBRARIES := 

LOCAL_MODULE:= legend

include $(BUILD_SHARED_LIBRARY)
