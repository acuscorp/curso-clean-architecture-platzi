package com.platzi.android.rickandmorty.api

import com.platzi.android.rickandmorty.data.RemoteCharacterDataSource
import com.platzi.android.rickandmorty.data.RemoteEpisodeDataSource
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterRetrofitDataSource(
    private val characterRequest: CharacterRequest
): RemoteCharacterDataSource {

    override fun getAllCharacters(page: Int): Single<List<Character>> {
        return characterRequest
            .getService<CharacterService>()
            .getAllCharacters(page)
            .map(CharacterResponseServer::toCharacterDomainList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}

//TODO Paso 4: Pasar como parámetro "episodeRequest" de tipo EpisodeRequest
//TODO Paso 4.1: Implementar la interfaz para fuente de datos remoto de episodio creada en el Paso 1
class EpisodeRetrofitDataSource(
    private val episodeRequest: EpisodeRequest
) : RemoteEpisodeDataSource{
    override fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> {
        return Observable.fromIterable(episodeUrlList)
            .flatMap { episode->
                episodeRequest.baseUrl = episode
                episodeRequest
                    .getService<EpisodeService>()
                    .getEpisode()
                    .map(EpisodeServer::toEpisodeDomain)
                    .toObservable()
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

}
