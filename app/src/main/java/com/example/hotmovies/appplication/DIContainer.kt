package com.example.hotmovies.appplication

//class DIContainer(val appContext: Context) {
//
//    private val tmdbMovieDataApiService: TmdbMovieDataApiInterface by lazy {
//        TmdbMovieDataApiServiceFactory.create()
//    }
//
//    private val tmdbMovieImageApiService: TmdbMovieImageApiInterface by lazy {
//        TmdbMovieImageApiFactory.create()
//    }
//
//    private val authenticationApiService: TmdbAuthenticationApiInterface by lazy {
//        TmdbAuthenticationApiFactory.create()
//    }
//
//    private val sessionProvider: SessionProviderInterface by lazy {
//        TmdbSessionProvider(
//            appContext,
//            appContext.getString(R.string.redirect_uri),
//            authenticationApiService
//        )
//    }
//
//    val loginRepository: LoginRepositoryInterface by lazy {
//        LoginRepository(appContext, sessionProvider)
//    }
//
//    private val crypto: CryptoInterface by lazy {
//        Crypto()
//    }
//
//    val secureRepository: SecureRepositoryInterface by lazy {
//        SecureRepository(crypto, dataStore)
//    }
//
//    private val dataStore: DataStore<Preferences> by lazy {
//        PreferenceDataStoreFactory.create(
//            produceFile = { appContext.preferencesDataStoreFile(appContext.getString(R.string.secure_datastore_file)) }
//        )
//    }
//    val pager: Pager<Int, Movie> by lazy {
//        val pagingConfig = PagingConfig(20, enablePlaceholders = false)
//        Pager(
//            config = pagingConfig,
//            pagingSourceFactory = {
//                MoviePagingSource(
//                    movieDataRepository,
//                    Dispatchers.IO
//                )
//            }
//        )
//    }
//
//    val movieImageIdToUrlMapper: MovieImageIdToUrlMapperInterface by lazy {
//        TmdbMovieIdToUrlMapper()
//    }
//
//    val movieImageRepository: MovieImageRepositoryInterface by lazy {
//        TmdbMovieImageRepository(tmdbMovieImageApiService)
//    }
//
//    val _movieImageRepository: MovieImageRepositoryInterface by lazy {
//        MockMovieImageRepository(appContext, R.drawable.vector_background)
//    }
//
//    val movieDataRepository: MovieDataRepositoryInterface by lazy {
//        TmdbMovieDataRepository(
//            appContext,
//            tmdbMovieDataApiService,
//            movieImageRepository,
//            secureRepository
//        )
//    }
//
//    val _movieDataRepository: MovieDataRepositoryInterface by lazy {
//        MockMovieDataRepository(appContext, R.drawable.vector_background, true)
//    }
//
//    val previewMovieDataRepository: MovieDataRepositoryInterface by lazy {
//        MockMovieDataRepository(appContext, R.drawable.vector_background, false)
//    }
//}