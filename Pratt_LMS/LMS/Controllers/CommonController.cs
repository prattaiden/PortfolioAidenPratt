using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Build.Experimental.ProjectCache;
using Microsoft.Build.Framework;
using Microsoft.CodeAnalysis.CSharp.Syntax;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS.Controllers
{
    public class CommonController : Controller
    {
        private readonly LMSContext db;

        public CommonController(LMSContext _db)
        {
            db = _db;
        }

        /*******Begin code to modify********/

        /// <summary>
        /// Retreive a JSON array of all departments from the database.
        /// Each object in the array should have a field called "name" and "subject",
        /// where "name" is the department name and "subject" is the subject abbreviation.
        /// </summary>
        /// <returns>The JSON array</returns>
        public IActionResult GetDepartments()
        {      

            var departments = from Department in db.Departments select Department;

            return Json(departments.ToArray());
        }



        /// <summary>
        /// Returns a JSON array representing the course catalog.
        /// Each object in the array should have the following fields:
        /// "subject": The subject abbreviation, (e.g. "CS")
        /// "dname": The department name, as in "Computer Science"
        /// "courses": An array of JSON objects representing the courses in the department.
        ///            Each field in this inner-array should have the following fields:
        ///            "number": The course number (e.g. 5530)
        ///            "cname": The course name (e.g. "Database Systems")
        /// </summary>
        /// <returns>The JSON array</returns>
        public IActionResult GetCatalog()
        {            
            var catalog = from d in db.Departments
                select new{
                    subject = d.Subject, dname = d.Name, 
                        courses = (from c in db.Courses
                            where c.Department == d.Subject
                                select new{
                                    number = c.Number,
                                    cname = c.Name
                                }).ToArray()
                };
            return Json(catalog);
        }

        /// <summary>
        /// Returns a JSON array of all class offerings of a specific course.
        /// Each object in the array should have the following fields:
        /// "season": the season part of the semester, such as "Fall"
        /// "year": the year part of the semester
        /// "location": the location of the class
        /// "start": the start time in format "hh:mm:ss"
        /// "end": the end time in format "hh:mm:ss"
        /// "fname": the first name of the professor
        /// "lname": the last name of the professor
        /// </summary>
        /// <param name="subject">The subject abbreviation, as in "CS"</param>
        /// <param name="number">The course number, as in 5530</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetClassOfferings(string subject, int number)
        {            
            var classes = from c in db.Classes
                where c.ListingNavigation.Number == number && c.ListingNavigation.Department == subject
                    select new {
                        season = c.Season, year = c.Year, location = c.Location, start = c.StartTime, end = c.EndTime,
                            Fname = c.TaughtByNavigation.FName, Lname = c.TaughtByNavigation.LName
                    };

            return Json(classes);
        }

        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <returns>The assignment contents</returns>
        public IActionResult GetAssignmentContents(string subject, int num, string season, int year, string category, string asgname)
        {            
          
                var query = from assignment in db.Assignments
                where assignment.CategoryNavigation.InClassNavigation.Season == season && assignment.CategoryNavigation.InClassNavigation.Year == year
                    && assignment.CategoryNavigation.InClassNavigation.ListingNavigation.Department == subject
                        && assignment.CategoryNavigation.InClassNavigation.ListingNavigation.Number == num && assignment.Name == asgname
                            select assignment.Contents;

            
            var assignmentContent = query.FirstOrDefault();

            
            if(assignmentContent != null){

        
                return Content(assignmentContent);
            }
            else{
                return Content("WRONG ASS CONTenT");
            }
        }


        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment submission.
        /// Returns the empty string ("") if there is no submission.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <param name="uid">The uid of the student who submitted it</param>
        /// <returns>The submission text</returns>
        public IActionResult GetSubmissionText(string subject, int num, string season, int year, string category, string asgname, string uid)
        {            
               var query = from s in db.Submissions 
               where s.Student == uid && s.AssignmentNavigation.Name == asgname
                    && s.AssignmentNavigation.CategoryNavigation.Name == category
                        && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.Season == season
                            && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.Year == year
                                && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.ListingNavigation.Number == num
                                    && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.ListingNavigation.Department == subject
                                        select s.SubmissionContents;
            
            var submissionText = query.FirstOrDefault();
  
            if(submissionText != null){
                return Content(submissionText);
            }
            else{
                return Content("WRONG SUB CONTENT");
            }
        }


        /// <summary>
        /// Gets information about a user as a single JSON object.
        /// The object should have the following fields:
        /// "fname": the user's first name
        /// "lname": the user's last name
        /// "uid": the user's uid
        /// "department": (professors and students only) the name (such as "Computer Science") of the department for the user. 
        ///               If the user is a Professor, this is the department they work in.
        ///               If the user is a Student, this is the department they major in.    
        ///               If the user is an Administrator, this field is not present in the returned JSON
        /// </summary>
        /// <param name="uid">The ID of the user</param>
        /// <returns>
        /// The user JSON object 
        /// or an object containing {success: false} if the user doesn't exist
        /// </returns>
        public IActionResult GetUser(string uid)
        {           
            var admin = from a in db.Administrators
                where a.UId == uid
                select a;
            var prof = from p in db.Professors
                where p.UId == uid
                select p;
            var stud = from s in db.Students
                where s.UId == uid
                select s;
            
            if(admin.Count() > 0){
                Administrator res = admin.FirstOrDefault();
                var a = new {
                    fname = res.FName, lname = res.LName, uid = res.UId
                };
                return Json(a);
            }
             if(prof.Count() > 0){
                Professor res = prof.FirstOrDefault();
                var p = new {
                    fname = res.FName, lname = res.LName, uid = res.UId, department = res.WorksIn
                };
                return Json(p);
            }
                if(stud.Count() > 0){
                Student res = stud.FirstOrDefault();
                var s = new {
                    fname = res.FName, lname = res.LName, uid = res.UId, department = res.Major
                };
                return Json(s);
            }
            else{
                return Json(new { success = false });
            }
        }


        /*******End code to modify********/
    }
}

