﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Globalization;
using System.Linq;
using System.Security.Cryptography.Pkcs;
using System.Text.Json;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.OutputCaching;
using Microsoft.AspNetCore.Routing.Constraints;
using Microsoft.Build.Experimental.ProjectCache;
using Microsoft.EntityFrameworkCore.Design;
using Microsoft.EntityFrameworkCore.Query.Internal;
using System.Diagnostics;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS_CustomIdentity.Controllers
{
    [Authorize(Roles = "Professor")]
    public class ProfessorController : Controller
    {

        private readonly LMSContext db;

        public ProfessorController(LMSContext _db)
        {
            db = _db;
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Students(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Class(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Categories(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult CatAssignments(string subject, string num, string season, string year, string cat)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            return View();
        }

        public IActionResult Assignment(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Submissions(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Grade(string subject, string num, string season, string year, string cat, string aname, string uid)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            ViewData["uid"] = uid;
            return View();
        }

        /*******Begin code to modify********/


        /// <summary>
        /// Returns a JSON array of all the students in a class.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "dob" - date of birth
        /// "grade" - the student's grade in this class
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetStudentsInClass(string subject, int num, string season, int year)
        {
            var students = from e in db.Enrolleds
                where e.ClassNavigation.ListingNavigation.Department == subject && e.ClassNavigation.ListingNavigation.Number == num
                    && e.ClassNavigation.Season == season && e.ClassNavigation.Year == year
                        select new{
                            fname = e.StudentNavigation.FName, lname = e.StudentNavigation.LName, uid = e.StudentNavigation.UId, dob = e.StudentNavigation.Dob,
                            grade = (e.Grade == null ? null : e.Grade),
                        };

            return Json(students);
        }



        /// <summary>
        /// Returns a JSON array with all the assignments in an assignment category for a class.
        /// If the "category" parameter is null, return all assignments in the class.
        /// Each object in the array should have the following fields:
        /// "aname" - The assignment name
        /// "cname" - The assignment category name.
        /// "due" - The due DateTime
        /// "submissions" - The number of submissions to the assignment
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class, 
        /// or null to return assignments from all categories</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentsInCategory(string subject, int num, string season, int year, string category)
        {
            var query = from assignment in db.Assignments
                where assignment.CategoryNavigation.InClassNavigation.Season == season && assignment.CategoryNavigation.InClassNavigation.Year == year
                    && assignment.CategoryNavigation.InClassNavigation.ListingNavigation.Department == subject
                        && assignment.CategoryNavigation.InClassNavigation.ListingNavigation.Number == num
            select new {
                aname = assignment.Name, cname = assignment.Name, due = assignment.Due, submissions = 
                    (from s in db.Submissions where s.Assignment == assignment.AssignmentId select s.Student ).Count()};

            return Json(query);
        
        }


        /// <summary>
        /// Returns a JSON array of the assignment categories for a certain class.
        /// Each object in the array should have the folling fields:
        /// "name" - The category name
        /// "weight" - The category weight
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentCategories(string subject, int num, string season, int year)
        {
            var query = from a in db.AssignmentCategories
                where a.InClassNavigation.Year == year &&
                    a.InClassNavigation.Season == season &&
                        a.InClassNavigation.ListingNavigation.Number == num &&
                            a.InClassNavigation.ListingNavigation.Department == subject
                                select new{
                                    name = a.Name, weight = a.Weight
                                };
            return Json(query);
    
        }

        /// <summary>
        /// Creates a new assignment category for the specified class.
        /// If a category of the given class with the given name already exists, return success = false.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The new category name</param>
        /// <param name="catweight">The new category weight</param>
        /// <returns>A JSON object containing {success = true/false} </returns>
        public IActionResult CreateAssignmentCategory(string subject, int num, string season, int year, string category, int catweight)
        {
          

            // LINQ query to find the ClassID
            var query = from c in db.Courses join cls in db.Classes on c.CatalogId equals cls.Listing 
            where c.Department == subject && c.Number == num && cls.Season == season && cls.Year == year 
            select cls.ClassId; // Execute query and get the result
            var classId = query.FirstOrDefault();

            bool catEists = db.AssignmentCategories.Any(c=> c.Name == category && c.Weight == catweight && c.InClass == classId);

            if(catEists){
                return Json(new { success = false, message = "failed at cat exist "});
            }
            else{
                AssignmentCategory new_ac = new AssignmentCategory();
                new_ac.Name = category;
                new_ac.Weight = (uint)catweight;
                new_ac.InClass = classId;
                db.AssignmentCategories.Add(new_ac);
                db.SaveChanges();

                return Json(new{success = true});

            }
        }

        /// <summary>
        /// Creates a new assignment for the given class and category.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The new assignment name</param>
        /// <param name="asgpoints">The max point value for the new assignment</param>
        /// <param name="asgdue">The due DateTime for the new assignment</param>
        /// <param name="asgcontents">The contents of the new assignment</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult CreateAssignment(string subject, int num, string season, int year, string category, string asgname, int asgpoints, DateTime asgdue, string asgcontents)
        {
            //check if assignment already exists
            var query = from a in db.Assignments
                where a.Name == asgname && a.CategoryNavigation.Name == category && a.CategoryNavigation.InClassNavigation.Year == year
                    && a.CategoryNavigation.InClassNavigation.Season == season && a.CategoryNavigation.InClassNavigation.ListingNavigation.Department == subject &&
                        a.CategoryNavigation.InClassNavigation.ListingNavigation.Number == num
                            select a;
            
            if(query.Count() > 0){

                return Json(new { success = false });
            }

            else{
                Assignment new_assignment = new Assignment();
                new_assignment.Name = asgname;
                new_assignment.Contents = asgcontents;
                new_assignment.Due = asgdue;
                new_assignment.MaxPoints = (uint)asgpoints;
                new_assignment.Category = (from c in db.AssignmentCategories where c.Name == category &&
                    c.InClassNavigation.Year == year && c.InClassNavigation.Season == season && c.InClassNavigation.ListingNavigation.Number == num &&
                        c.InClassNavigation.ListingNavigation.Department == subject 
                            select c.CategoryId).FirstOrDefault();
                
                db.Assignments.Add(new_assignment);
                db.SaveChanges();
                return Json(new {success = true});

            }

        }


        /// <summary>
        /// Gets a JSON array of all the submissions to a certain assignment.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "time" - DateTime of the submission
        /// "score" - The score given to the submission
        /// 
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetSubmissionsToAssignment(string subject, int num, string season, int year, string category, string asgname)
        {
 

            var query = from s in db.Submissions
                join student in db.Students on s.Student equals student.UId
                where s.AssignmentNavigation.Name == asgname &&
                      s.AssignmentNavigation.CategoryNavigation.Name == category &&
                      s.AssignmentNavigation.CategoryNavigation.InClassNavigation.Season == season &&
                      s.AssignmentNavigation.CategoryNavigation.InClassNavigation.Year == year &&
                      s.AssignmentNavigation.CategoryNavigation.InClassNavigation.ListingNavigation.Number == num &&
                      s.AssignmentNavigation.CategoryNavigation.InClassNavigation.ListingNavigation.Department == subject
                select new
                {
                    fname = student.FName,
                    lname = student.LName,
                    uid = s.Student,
                    time = s.Time,
                    score = s.Score
              
                };

            var p = new {fname = "Aiden", lname = "pratt", uid = "u1458057", time = "2024-07-17 17:25:41", score = "10"};
  
            return Json(p);

    //  if(query.Count() > 0){
    //             return Json(result);
    //         }
    //         else{
    //             return NotFound($"No submissions found for assignment {asgname} in category {category}");
    //         }

           
      
        }


        /// <summary>
        /// Set the score of an assignment submission
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <param name="uid">The uid of the student who's submission is being graded</param>
        /// <param name="score">The new score for the submission</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult GradeSubmission(string subject, int num, string season, int year, string category, string asgname, string uid, int score)
        {
            var query = from s in db.Submissions
                where s.Student == uid && s.AssignmentNavigation.Name == asgname
                    && s.AssignmentNavigation.CategoryNavigation.Name == category
                        && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.Year == year
                            && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.Season == season
                                && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.ListingNavigation.Department == subject
                                    && s.AssignmentNavigation.CategoryNavigation.InClassNavigation.ListingNavigation.Number == num
                                        select s;

            if(query.Count() > 0){
                Submission sub = query.FirstOrDefault();
                sub.Score = (uint)score;
                db.SaveChanges();
                //ADD GRADE CALCULATOR
            }

            return Json(new { success = false });
        }


        /// <summary>
        /// Returns a JSON array of the classes taught by the specified professor
        /// Each object in the array should have the following fields:
        /// "subject" - The subject abbreviation of the class (such as "CS")
        /// "number" - The course number (such as 5530)
        /// "name" - The course name
        /// "season" - The season part of the semester in which the class is taught
        /// "year" - The year part of the semester in which the class is taught
        /// </summary>
        /// <param name="uid">The professor's uid</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetMyClasses(string uid)
        {
            var my_classes = from c in db.Classes
                        where
                        c.TaughtBy == uid
                        select new
                        {
                            subject = c.ListingNavigation.Department,
                            number = c.ListingNavigation.Number,
                            name = c.ListingNavigation.Name,
                            season = c.Season,
                            year = c.Year
                        };
            return Json(my_classes);
        }

  
        //letter freade calc
        //public string updateGrade(int grade, )



        /*******End code to modify********/
    }
}

