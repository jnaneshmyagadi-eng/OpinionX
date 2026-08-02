sed -i '46c\
    val follows: StateFlow<List<FollowEntity>> = repository.allFollows\
        .stateIn(\
            scope = viewModelScope,\
            started = SharingStarted.WhileSubscribed(5000),\
            initialValue = emptyList()\
        )\
\
    val userProfile: StateFlow<UserEntity?> = repository.userProfile\
        .stateIn(\
            scope = viewModelScope,\
            started = SharingStarted.WhileSubscribed(5000),\
            initialValue = null\
        )\
\
    private val _mockUsers = MutableStateFlow<List<UserProfile>>(emptyList())\
' /app/applet/app/src/main/java/com/example/viewmodel/OpinionViewModel.kt

sed -i 's/followedUserId/followedId/g' /app/applet/app/src/main/java/com/example/viewmodel/OpinionViewModel.kt
