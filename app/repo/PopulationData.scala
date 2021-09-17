package repo

case class PopulationData(
                         countryOrArea: String,
                         year: Short,
                         area: String,
                         sex: String,
                         city: String,
                         cityType: String,
                         recordType: String,
                         reliability: String,
                         sourceYear: Short,
                         value: Double,
                         valueFootnotes:String
                         )
