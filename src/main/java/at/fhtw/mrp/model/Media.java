package at.fhtw.mrp.model;

public abstract class Media {
        private final int id;
        private final String title;
        private final String description;
        private final int releaseYear;
        private final int ageRestriction;

        public Media(
                int id,
                String title,
                String description,
                int releaseYear,
                int ageRestriction
        )
        {
            this.id = id;
            this.title = title;
            this.description = description;
            this.releaseYear = releaseYear;
            this.ageRestriction = ageRestriction;
        }

        public int getId(){
            return id;
        }

        public String getTitle(){
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getReleaseYear() {
            return releaseYear;
        }

        public int getAgeRestriction(){
            return ageRestriction;
        }
    }

