package com.aarcosg.copdhelp.interactor;

import javax.inject.Inject;

public class COPDHelpServiceInteractorImpl implements COPDHelpServiceInteractor {

    private static final String TAG = COPDHelpServiceInteractorImpl.class.getCanonicalName();

    /*private final SharedPreferences mPrefs;
    private final RxNetwork mRxNetwork;
    private final HermesCitizenApi mHermesCitizenApi;
    private final ZtreamyApi mZtreamyApi;*/

    @Inject
    public COPDHelpServiceInteractorImpl(){}

   /* @Inject
    public COPDHelpServiceInteractorImpl(
            SharedPreferences sharedPreferences,
            RxNetwork rxNetwork,
            HermesCitizenApi hermesCitizenApi,
            ZtreamyApi ztreamyApi){
        this.mPrefs = sharedPreferences;
        this.mRxNetwork = rxNetwork;
        this.mHermesCitizenApi = hermesCitizenApi;
        this.mZtreamyApi = ztreamyApi;
    }*/

    /*@Override
    public DataReadRequest.Builder buildGoogleFitLocationsRequest() {
        DataReadRequest.Builder builder = new DataReadRequest.Builder()
                .read(DataType.TYPE_LOCATION_SAMPLE);
        return builder;
    }

    @Override
    public DataReadRequest.Builder buildGoogleFitActivitiesRequest() {
        DataReadRequest.Builder builder = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY);
        return builder;
    }

    @RxLogObservable
    @Override
    public Observable<List<LocationSampleFit>> queryLocationsToGoogleFit() {

        DataReadRequest.Builder dataReadRequestBuilder = buildGoogleFitLocationsRequest();

        long startTime = mPrefs.getLong(Constants.PROPERTY_LAST_LOCATION_TIME_SENT,Utils.getStartTimeRange(Constants.RANGE_DAY));
        long endTime = new Date().getTime();
        dataReadRequestBuilder.setTimeRange(startTime,endTime, TimeUnit.MILLISECONDS);

        DataReadRequest dataReadRequest = dataReadRequestBuilder.build();
        DataReadRequest dataReadRequestServer = dataReadRequestBuilder.enableServerQueries().build();

        return RxFit.checkConnection()
                .andThen(RxFit.History.read(dataReadRequestServer)
                        .compose(new RxFit.OnExceptionResumeNext.Single<>(RxFit.History.read(dataReadRequest)))
                        .flatMapObservable(dataReadResult -> Observable.just(
                                SyncServiceUtils.getLocationListFromDataSets(dataReadResult.getDataSets())
                        ))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()));
    }

    @RxLogObservable
    @Override
    public Observable<List<ActivitySegmentFit>> queryActivitiesToGoogleFit() {

        DataReadRequest.Builder dataReadRequestBuilder = buildGoogleFitActivitiesRequest();

        long startTime = mPrefs.getLong(Constants.PROPERTY_LAST_ACTIVITY_TIME_SENT,Utils.getStartTimeRange(Constants.RANGE_DAY));
        long endTime = new Date().getTime();
        dataReadRequestBuilder
                .setTimeRange(startTime,endTime, TimeUnit.MILLISECONDS)
                .bucketByActivitySegment(1,TimeUnit.MINUTES);

        DataReadRequest dataReadRequest = dataReadRequestBuilder.build();
        DataReadRequest dataReadRequestServer = dataReadRequestBuilder.enableServerQueries().build();

        return RxFit.checkConnection()
                .andThen(RxFit.History.read(dataReadRequestServer)
                        .compose(new RxFit.OnExceptionResumeNext.Single<>(RxFit.History.read(dataReadRequest)))
                        .flatMapObservable(dataReadResult -> Observable.just(
                                SyncServiceUtils.getActivitySegmentListFromBuckets(dataReadResult.getBuckets())
                        ))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()));
    }

    @Override
    public void uploadLocations(List<LocationSampleFit> locations) {
        ItemsList<LocationSampleFit> items = new ItemsList<>(
                getUserFromPreferences().getEmail(),
                locations);
        uploadLocationsToHermesCitizen(items);
        uploadLocationsToZtreamy(items);
        mPrefs.edit().putLong(Constants.PROPERTY_LAST_LOCATION_TIME_SENT,new Date().getTime()).commit();
    }

    @RxLogObservable
    private void uploadLocationsToHermesCitizen(ItemsList<LocationSampleFit> items){
        mRxNetwork.checkInternetConnection()
            .andThen(mHermesCitizenApi.uploadLocations(items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(integerResponse -> {
                    if(integerResponse.body() == HermesCitizenApi.RESPONSE_OK){
                            Log.i(TAG,"Locations uploaded to Hermes Citizen server");
                    }
                },
                throwable -> Log.e(TAG,"Locations not uploaded to Hermes Citizen server"));
    }

    @RxLogObservable
    private void uploadLocationsToZtreamy(ItemsList<LocationSampleFit> items){
        Map<String,Object> map = new HashMap<>(1);
        map.put(ZtreamyApi.EVENT_TYPE_LOCATIONS,items.getItems());
        Event ztreamyEvent = new Event(
                getHash(items.getUser()),
                ZtreamyApi.SYNTAX,
                ZtreamyApi.APPLICATION_ID,
                ZtreamyApi.EVENT_TYPE_LOCATIONS,
                map);
        mRxNetwork.checkInternetConnection()
                .andThen(mZtreamyApi.uploadLocations(ztreamyEvent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(response -> {
                    if(response.isSuccess()){
                        Log.i(TAG,"Locations uploaded to Ztreamy");
                    }
                },
                throwable -> Log.e(TAG,"Locations not uploaded to Ztreamy"));
    }

    @Override
    public void uploadActivities(List<ActivitySegmentFit> activities) {
        ItemsList<ActivitySegmentFit> items = new ItemsList<>(
                getUserFromPreferences().getEmail(),
                activities);
        uploadActivitiesToHermesCitizen(items);
        uploadActivitiesToZtreamy(items);
        mPrefs.edit().putLong(Constants.PROPERTY_LAST_ACTIVITY_TIME_SENT,new Date().getTime()).commit();
    }

    @RxLogObservable
    private void uploadActivitiesToHermesCitizen(ItemsList<ActivitySegmentFit> items){
        mRxNetwork.checkInternetConnection()
                .andThen(mHermesCitizenApi.uploadActivities(items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(integerResponse -> {
                    if(integerResponse.body() == HermesCitizenApi.RESPONSE_OK){
                        Log.i(TAG,"Activities uploaded to Hermes Citizen server");
                    }
                },
                throwable -> Log.e(TAG,"Activities not uploaded to Hermes Citizen server"));
    }

    @RxLogObservable
    private void uploadActivitiesToZtreamy(ItemsList<ActivitySegmentFit> items){
        Map<String,Object> map = new HashMap<>(1);
        map.put(ZtreamyApi.EVENT_TYPE_ACTIVITIES,items.getItems());
        Event ztreamyEvent = new Event(
                getHash(items.getUser()),
                ZtreamyApi.SYNTAX,
                ZtreamyApi.APPLICATION_ID,
                ZtreamyApi.EVENT_TYPE_ACTIVITIES,
                map);
        mRxNetwork.checkInternetConnection()
                .andThen(mZtreamyApi.uploadActivities(ztreamyEvent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(response -> {
                    if(response.isSuccess()){
                        Log.i(TAG,"Activities uploaded to Ztreamy");
                    }
                },
                throwable -> Log.e(TAG,"Activities not uploaded to Ztreamy"));
    }

    private String getHash(String stringToHash){
        String hashedEmail = "";
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            hashedEmail =  bin2hex(messageDigest.digest(stringToHash.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedEmail;
    }

    private String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }*/

}