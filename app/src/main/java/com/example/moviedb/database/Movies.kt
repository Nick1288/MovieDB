package com.example.moviedb.database

import com.example.moviedb.model.Movie
import com.example.moviedb.model.MovieCategory


class Movies {
    fun getMovies(category: MovieCategory): List<Movie> {
        return when (category) {
            MovieCategory.POPULAR -> listOf(
                Movie(278, "The Shawshank Redemption", "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg", "/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg", "1994-09-23", "Imprisoned in the 1940s for the double murder of his wife and her lover...", listOf("Drama"), "https://www.warnerbros.com/movies/shawshank-redemption", "tt0111161"),
                Movie(238, "The Godfather", "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg", "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg", "1972-03-14", "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family...", listOf("Crime", "Drama"), "https://www.paramount.com/movies/godfather", "tt0068646"),
                Movie(372058, "Your Name.", "/8GJsy7w7frGquw1cy9jasOGNNI1.jpg", "/8x9iKH8kWA0zdkgNdpAew7OstYe.jpg", "2016-08-26", "High schoolers Mitsuha and Taki are complete strangers living separate lives. But one night, they suddenly switch places...", listOf("Animation", "Drama", "Fantasy", "Romance"), "https://www.toho.co.jp/movie/lineup/yourname.html", "tt5311514"),
                Movie(122, "The Lord of the Rings: The Return of the King", "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg", "/2u7zbn8EudG6kLlBzUYqP8RyFU4.jpg", "2003-12-17", "As armies mass for a final battle that will decide the fate of the world...", listOf("Adventure", "Drama", "Fantasy"), "https://www.lordoftherings.net/", "tt0167260"),
                Movie(13, "Forrest Gump", "/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg", "/ghgfzbEV7kbpbi1O8eIILKVXEA8.jpg", "1994-06-23", "A man with a low IQ has accomplished great things in his life...", listOf("Drama", "Romance"), "https://www.paramount.com/movies/forrest-gump", "tt0109830")  )

            MovieCategory.TOP_RATED -> listOf(
                Movie(240, "The Godfather Part II", "/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg", "/kGzFbGhp99zva6oZODW5atUtnqi.jpg", "1974-12-20", "In the continuing saga of the Corleone crime family...", listOf("Crime", "Drama"), "https://www.paramount.com/movies/godfather-part-ii", "tt0071562"),
                Movie(424, "Schindler's List", "/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg", "/zb6fM1CX41D9rF9hdgclu0peUmy.jpg", "1993-12-15", "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives...", listOf("Biography", "Drama", "History"), "https://www.universalpictures.com/movies/schindlers-list", "tt0108052"),
                Movie(429, "The Good, the Bad and the Ugly", "/bX2xnavhMYjWDoZp1VM6VnU1xwe.jpg", "/x4biAVdPVCghBlsVIzB6NmbghIz.jpg", "1966-12-22", "While the Civil War rages on...", listOf("Adventure", "Western"), "https://www.mgm.com/movies/the-good-the-bad-and-the-ugly", "tt0060196"),
                Movie(769, "GoodFellas", "/aKuFiU82s5ISJpGZp7YkIr3kCUd.jpg", "/7TF4p86ZafnxFuNqWdhpHXFO244.jpg", "1990-09-12", "The true story of Henry Hill, a half-Irish, half-Sicilian Brooklyn kid...", listOf("Biography", "Crime", "Drama"), "https://www.warnerbros.com/movies/goodfellas", "tt0099685"),
                Movie(346, "Seven Samurai", "/8OKmBV5BUFzmozIC3pPWKHy17kx.jpg", "/sJNNMCc6B7KZIY3LH3JMYJJNH5j.jpg", "1954-04-26", "A samurai answers a village's request for protection...", listOf("Action", "Drama"), "https://www.toho.co.jp/movie/lineup/sevensamurai.html", "tt0047478"))

            MovieCategory.FAVORITES -> listOf(
                Movie(155, "The Dark Knight", "/qJ2tW6WMUDux911r6m7haRef0WH.jpg", "/oOv2oUXcAaNXakRqUPxYq5lJURz.jpg", "2008-07-16", "Batman raises the stakes in his war on crime...", listOf("Action", "Crime", "Drama", "Thriller"), "https://www.warnerbros.com/movies/dark-knight", "tt0468569"),
                Movie(19404, "Dilwale Dulhania Le Jayenge", "/lfRkUr7DYdHldAqi3PwdQGBRBPM.jpg", "/90ez6ArvpO8bvpyIngBuwXOqJm5.jpg", "1995-10-20", "Raj is a rich, carefree, happy-go-lucky second generation NRI...", listOf("Drama", "Romance", "Musical"), "https://www.yashrajfilms.com/movies/dilwale-dulhania-le-jayenge", "tt0112870"),
                Movie(497, "The Green Mile", "/8VG8fDNiy50H4FedGwdSVUPoaJe.jpg", "/vxJ08SvwomfKbpboCWynC3uqUg4.jpg", "1999-12-10", "A supernatural tale set on death row in a Southern prison...", listOf("Crime", "Drama", "Fantasy"), "https://www.warnerbros.com/movies/green-mile", "tt0120689"),
                Movie(12477, "Grave of the Fireflies", "/k9tv1rXZbOhH7eiCk378x61kNQ1.jpg", "/tDFvXn4tane9lUvFAFAUkMylwSr.jpg", "1988-04-16", "In the final months of World War II, 14-year-old Seita and his sister Setsuko are orphaned...", listOf("Animation", "Drama", "War"), "https://www.ghibli.jp/works/hotaru/", "tt0095327"),
                Movie(637, "Life Is Beautiful", "/74hLDKjD5aGYOotO6esUVaeISa2.jpg", "/gavyCu1UaTaTNPsVaGXT6pe5u24.jpg", "1997-12-20", "A touching story of an Italian book seller of Jewish ancestry...", listOf("Comedy", "Drama", "Romance", "War"), "https://www.miramax.com/movie/life-is-beautiful/", "tt0118799")
            )
        }
    }
}